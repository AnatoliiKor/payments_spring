package com.kor.payments.controller;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("transaction")
public class TransactionController {
    private static final Logger log = LogManager.getLogger(TransactionController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("payment/{transaction}")
    public String getTransaction(
            @PathVariable Transaction transaction,
            Model model) {
        model.addAttribute("transaction", transaction);
        return "payment_details";
    }

    @GetMapping("/form")
    public String newAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        User user = (User) userDetails;
        List<Account> accounts = accountService.findAllActiveAccountsByUser(user);
        if (accounts.isEmpty()) {
            return "redirect:/wallet?message=no_accounts";
        }
        model.addAttribute("accounts", accounts);
        return "make_payment";
    }

    @PostMapping("/prepare")
    public String preparePayment(
            HttpSession httpSession,
            @RequestParam String payer,
            @RequestParam Long receiver,
            @RequestParam double amount,
            @RequestParam(required = false, defaultValue = "-") String destination,
            Model model) {
        int amountInt = (int) (amount * 100);
        Account payerAccount = accountService.findByNumberId(Long.parseLong(payer));
        Account receiverAccount = accountService.findByNumberId(receiver);
        if (receiverAccount == null || !receiverAccount.isActive()) {
            return "redirect:/transaction/form?warn=payment_receiver_not_found";
        }
        if (amountInt <= 0) {
            return "redirect:/transaction/form";
        }
        if (amountInt >= payerAccount.getBalance()) {
            return "redirect:/transaction/form?warn=not_enough&receiver=" +
                    receiver + "&amount=" + amountInt;
        }
        if (!receiverAccount.getCurrency().equals(payerAccount.getCurrency())) {
            return "redirect:/transaction/form?warn=not_currency&message=" + receiverAccount.getCurrency().name() + "&receiver=" +
                    receiver + "&amount=" + amountInt;
        }
        Transaction payment = new Transaction();
        payment.setPayer(payerAccount);
        payment.setReceiver(receiverAccount);
        payment.setCurrency(payerAccount.getCurrency());
        payment.setAmount(amountInt);
        payment.setBalanceAfter((int) payerAccount.getBalance() - amountInt);
        payment.setDestination(destination);
        httpSession.setAttribute("payment", payment);
        log.info("Payment to receiver {} is prepared for payer {}", receiver, payer);
        return "make_payment";
    }

    @PostMapping("/cancel")
    public String cancelPayment(HttpSession httpSession) {
        httpSession.removeAttribute("payment");
            return "redirect:/wallet?message=canceled";
    }

    @PostMapping("/confirm")
    public String confirmPayment(HttpSession httpSession) {
        Transaction payment = (Transaction) httpSession.getAttribute("payment");
        if (payment != null && transactionService.makeTransaction(payment)) {
            httpSession.removeAttribute("payment");
//            return "redirect:transactions?message=payment_success&account_type=payer&user_id=" + userId;
            log.info("Amount {} is deducted from the account {}", payment.getAmount(), payment.getPayer().getId());
            return "redirect:/wallet?message=payment_success";
        } else {
            httpSession.removeAttribute("payment");
            log.info("Transaction is failed");
            return "redirect:/wallet?warn=payment_fail";
        }
    }
}
