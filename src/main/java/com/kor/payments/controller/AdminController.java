package com.kor.payments.controller;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.User;
import com.kor.payments.services.AccountRequestService;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRequestService accountRequestService;
    @Autowired
    private AccountService accountService;
    private final Logger log = LogManager.getLogger(AdminController.class);

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        log.info("user list requested by admin");
        return "users_list";
    }

    @GetMapping("/accounts")
    public String getAllAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAllAccounts());
        log.info("accounts list requested by admin");
        return "accounts_list";
    }

    @GetMapping
    public String adminPage(
//            @RequestParam(required = false) String messaage,
            Model model
    ) {
        model.addAttribute("requests", accountRequestService.findAll());
        log.info("requests requested by admin");
        return "admin";
    }

    @PostMapping("/action/{account}")
    public String getUserAccounts(@PathVariable Account account, @RequestParam(name = "is_active") boolean action, Model model) {
        String message;
        if(accountService.setIsActive(account, action)) {
            message = "message=updated";
        }
        message = "warn=not_updated";
        log.info("Action is changed for acount {}", account.getId());
        return "redirect:/admin" + message;
    }

}
