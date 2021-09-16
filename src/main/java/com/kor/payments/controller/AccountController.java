package com.kor.payments.controller;

import com.kor.payments.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public String getAllAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAllAccounts());
        return "accounts_list";
    }
}
