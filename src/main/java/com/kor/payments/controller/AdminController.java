package com.kor.payments.controller;

import com.kor.payments.services.AccountRequestService;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRequestService accountRequestService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users_list";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("requests", accountRequestService.findAll());
        return "admin";
    }
}
