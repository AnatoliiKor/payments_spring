package com.kor.payments.controller;

import com.kor.payments.constants.Constant;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.CurrencyRateService;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CurrencyRateService currencyRateService;


    @GetMapping
    public String getWallet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = (User) userDetails;
        model.addAttribute(Constant.ACCOUNTS, accountService.findAccountsByUser(user));
        model.addAttribute("rates", currencyRateService.findAllSorted());
        return "wallet";
    }
}
