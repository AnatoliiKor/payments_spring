package com.kor.payments.controller;

import com.kor.payments.domain.Role;
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

import java.util.List;

@Controller
@RequestMapping("transactions")
public class TransactionsController {
    private static final Logger log = LogManager.getLogger(TransactionsController.class);
    List<Transaction> transactions;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("{user}/payments_sorted")
    public String getUserPaymentsSorted(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable (required = false) User user,
            @RequestParam(required = false, defaultValue = "registered") String sort,
            @RequestParam(required = false, defaultValue = "DESC") String order,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false) String inflows,
            Model model) {
        boolean lastPage = false;
        if (user == null || (!user.getEmail().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(Role.ADMIN))) {
            return "accessDenied";
        }
        if (page < 0) {
            page = 0;
        }
        if (user.getRole().equals(Role.ADMIN)) {
            transactions = transactionService.findAllPage(page, sort, order);
            model.addAttribute("title", "transactions");
        } else if ("inflows".equals(inflows)) {
            transactions = transactionService.findReceiverTransactionsPage(user, page, sort, order);
            model.addAttribute("title", "inflows");
        } else {
            transactions = transactionService.findPayerTransactionsPage(user, page, sort, order);
            model.addAttribute("title", "payments");
        }
        if (transactions.size() < 10) {
            lastPage = true;
        }
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("last_page", lastPage);
        model.addAttribute("transactions", transactions);
        log.info("Payments are requested for user {}", user.getId());
        return "payments_list_sorted";
    }

}