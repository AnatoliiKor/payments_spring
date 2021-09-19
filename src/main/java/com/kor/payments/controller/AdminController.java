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

import java.util.List;

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
    int page = 0;
    int maxPage = 1000;
    String sort = "email";
    String order = "ASC";
    List<User> users;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        if (page > maxPage) {
            --page;
        }
        users = userService.findAllPage(page, sort, order);
        model.addAttribute("users", users);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("page", page);

        if (users.size() < 10) {
            maxPage = page;
        }
        log.info("user list requested by admin");
        return "users_list";
    }

    @GetMapping("/users/page")
    public String pageUsers(@RequestParam int p) {
        if (p >= 0) {
            page = p;
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/sort")
    public String pageSort(@RequestParam String sort, @RequestParam String order) {
        this.page = 0;
        this.sort = sort;
        this.order = order;
        return "redirect:/admin/users";
    }

    @GetMapping("/accounts")
    public String getAllAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAllAccounts());
        log.info("accounts list requested by admin");
        return "accounts_list";
    }

    @GetMapping
    public String adminPage(
            Model model
    ) {
        model.addAttribute("requests", accountRequestService.findAll());
        log.info("requests requested by admin");
        return "admin";
    }

    @PostMapping("/action/{account}")
    public String changeAccountStatus(@PathVariable Account account, @RequestParam(name = "is_active") boolean action, Model model) {
        String message;
        if(accountService.setIsActive(account, action)) {
            message = "?message=updated";
            if (account.getAccountRequest() == null) {
                return "redirect:/accounts" + message;
            }
        } else {
            message = "?warn=not_updated";
        }
        log.info("Action is changed for acount {}", account.getId());
        return "redirect:/admin" + message;
    }

    @PostMapping("/user_active/{user}")
    public String changeUserStatus(@PathVariable User user, @RequestParam(name = "is_active") boolean action, Model model) {
        String message;
        if(userService.setIsActive(user, action)) {
            message = "?message=updated";
        } else {
            message = "?warn=not_updated";
        }
        return "redirect:/admin/users" + message;
    }

}
