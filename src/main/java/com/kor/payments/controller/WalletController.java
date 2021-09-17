package com.kor.payments.controller;

import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String getWallet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        User user = (User) userDetails;
//        user.getAccounts();
//        model.addAttribute("accounts", accountService.findAllAccounts());
        return "wallet";
    }

    @PostMapping("/new_account")
    public String newAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "account_name") String accountName,
            @RequestParam String currency,
            Model model) {
        User user = (User) userDetails;
        if (accountService.newAccount(accountName, currency, user)) {
            return "redirect:/accounts";
        } else {
            model.addAttribute("warn", "account_not_opened");
            return "/wallet";
        }
    }

}
