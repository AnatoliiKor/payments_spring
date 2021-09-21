package com.kor.payments.controller;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Role;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("transactions")
public class TransactionsController {
    private static final Logger log = LogManager.getLogger(TransactionsController.class);
    int page = 0;
    int maxPage = 1000;
    String sort = "registered";
    String order = "ASC";
    List<Transaction> transactions;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("{user}/payments")
    public String getUserPayments(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable User user,
            Model model) {
        if (!user.getEmail().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(Role.ADMIN)) {
            return "accessDenied";
        }
        List<Transaction> transactions = transactionService.findPayerTransactions(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "payments");
        log.info("Payments are requested for user {}", user.getId());
        return "user_payments_list";
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
        return "user_payments_list";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getTransactions(Model model) {
        if (page > maxPage) {
            --page;
        }
        transactions = transactionService.findAllPage(page, sort, order);
        model.addAttribute("transactions", transactions);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        if (transactions.size() < 10) {
            maxPage = page;
        }
        log.info("Transactions are requested by Admin, page {}, sort {}, order {}", page, sort, order);
        return "payments_list";
    }

    @GetMapping("/page")
    public String pageAccounts(@RequestParam int p) {
        if (p >= 0) {
            page = p;
        }
        return "redirect:/transactions";
    }

    @GetMapping("/sort")
    public String pageSort(@RequestParam String sort, @RequestParam String order) {
        this.page = 0;
        this.sort = sort;
        this.order = order;
        return "redirect:/transactions";
    }
}