package com.kor.payments.controller;

import com.itextpdf.text.DocumentException;
import com.kor.payments.constants.Constant;
import com.kor.payments.domain.Account;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.CurrencyRateService;
import com.kor.payments.services.PDFBuilderService;
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("transaction")
public class TransactionController {
    private static final Logger log = LogManager.getLogger(TransactionController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CurrencyRateService currencyRateService;
    @Autowired
    private PDFBuilderService pdfBuilderService;


    @GetMapping("payment/{transaction}")
    public String getTransaction(
            @PathVariable Transaction transaction,
            Model model) {
        model.addAttribute(Constant.TRANSACTION, transaction);
        return "payment_details";
    }

    @GetMapping("/form")
    public String newAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        List<Account> accounts = accountService.findAllActiveAccountsByUser((User) userDetails);
        if (accounts.isEmpty()) {
            return "redirect:/wallet?message=no_accounts";
        }
        model.addAttribute(Constant.ACCOUNTS, accounts);
        return "make_payment";
    }

    @GetMapping("/prepare")
    public String preparePayment(
            HttpSession httpSession,
            @RequestParam long payer,
            @RequestParam long receiver,
            @RequestParam double amount,
            @RequestParam(required = false, defaultValue = "-") String destination,
            Model model) {
        int amountReceiver = (int) (amount * 100);
        Account receiverAccount = accountService.findByNumberId(receiver);

        String redirect = transactionService.checkReceiver(amountReceiver, receiverAccount);
        if (!Constant.CHECKED.equals(redirect)) return redirect;

        Account payerAccount = accountService.findByNumberId(payer);
        int amountPayer = amountReceiver;
        if (!receiverAccount.getCurrency().equals(payerAccount.getCurrency())) {
            amountPayer = currencyRateService.doExchange(payerAccount.getCurrency(), receiverAccount.getCurrency(), amountReceiver);
            model.addAttribute(Constant.WARN, "payment_attention_currency");
        }
        if (amountPayer >= payerAccount.getBalance()) {
            return "redirect:/transaction/form?warn=not_enough&receiver=" +
                    receiver + "&amount=" + amountReceiver;
        }
        Transaction payment = new Transaction();
        payment.setPayer(payerAccount);
        payment.setReceiver(receiverAccount);
        payment.setCurrency(payerAccount.getCurrency());
        payment.setAmount(amountPayer);
        payment.setAccrual(amountReceiver);
        payment.setDestination(destination);
        httpSession.setAttribute(Constant.PAYMENT, payment);
        log.info("Payment to receiver {} is prepared for payer {}", receiver, payer);
        return "make_payment";
    }

    @PostMapping("/cancel")
    public String cancelPayment(HttpSession httpSession) {
        httpSession.removeAttribute(Constant.PAYMENT);
        return "redirect:/wallet?message=canceled";
    }

    @PostMapping("/confirm")
    public String confirmPayment(HttpSession httpSession) {
        Transaction payment = (Transaction) httpSession.getAttribute("payment");
        if (payment != null && transactionService.makeTransaction(payment)) {
            httpSession.removeAttribute(Constant.PAYMENT);
            log.info("Amount {} is deducted from the account {}", payment.getAmount(), payment.getPayer().getId());
            return "redirect:/wallet?message=payment_success";
        } else {
            httpSession.removeAttribute(Constant.PAYMENT);
            log.info("Transaction is failed");
            return "redirect:/wallet?warn=payment_fail";
        }
    }

    @GetMapping("check/{transaction}")
    public String getCheck(@PathVariable Transaction transaction, Model model) {

        try {
            if (pdfBuilderService.getCheck(transaction)) model.addAttribute(Constant.MESSAGE, "successfully");
        } catch (IOException | DocumentException | URISyntaxException e) {
            log.debug("Check is not created due to {}", e.getMessage());
            model.addAttribute(Constant.WARN, "failed");
        }
        return "payment_details";
    }
}
