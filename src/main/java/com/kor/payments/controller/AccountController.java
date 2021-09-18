package com.kor.payments.controller;

import com.kor.payments.domain.User;
import com.kor.payments.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    private static final Logger log = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/{user}")
    public String getUserAccounts(@PathVariable User user, Model model) {
        model.addAttribute("accounts", user.getAccounts() );
//        model.addAttribute("accounts", accountService.findAccountsByUser(user));
        log.info("Accounts are requested by user {}", user.getId());
        return "accounts_list";
    }

    @PostMapping("/new_account")
    public String newAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "account_name") String accountName,
            @RequestParam String currency,
            Model model) {
        User user = (User) userDetails;
        if (accountService.newAccount(accountName, currency, user)) {
            log.info("Account {} is added to DB", accountName);
            return "redirect:/";
        } else {
            log.info("Account {} is not added to DB", accountName);
            model.addAttribute("warn", "account_not_opened");
            return "/wallet";
        }
    }
}
