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
    //    int page = 0;
//    int maxPage = 1000;
//    String sort = "registered";
//    String order = "ASC";
    List<Transaction> transactions;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("{user}/payments_sorted")
    public String getUserPaymentsSorted(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable User user,
            @RequestParam(required = false, defaultValue = "registered") String sort,
            @RequestParam(required = false, defaultValue = "DESC") String order,
            @RequestParam(required = false, defaultValue = "0") int page,
            Model model) {
        boolean lastPage = false;
        if (!user.getEmail().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(Role.ADMIN)) {
            return "accessDenied";
        }
        if (page < 0) {
            page = 0;
        }
        if (userDetails.getAuthorities().contains(Role.ADMIN)) {
            transactions = transactionService.findAllPage(page, sort, order);
        } else {
            transactions = transactionService.findPayerTransactionsPage(user, page, sort, order);
        }
        if (transactions.size() < 10) {
            lastPage = true;
        }
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("last_page", lastPage);
        model.addAttribute("transactions", transactions);
        if (!userDetails.getAuthorities().contains(Role.ADMIN)) {
            model.addAttribute("title", "payments");
        } else {model.addAttribute("title", "transactions");}
        log.info("Payments are requested for user {}", user.getId());
        return "user_payments_list_sorted";
    }


    @GetMapping("{user}/inflows")
    public String getUserInflows(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable User user,
            Model model) {
        if (!user.getEmail().equals(userDetails.getUsername())) {
            return "accessDenied";
        }
        List<Transaction> transactions = transactionService.findReceiverTransactions(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "inflows");
        log.info("Inflows are requested for user {}", user.getId());
        return "-user_payments_list";
    }

}