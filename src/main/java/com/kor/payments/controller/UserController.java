package com.kor.payments.controller;

import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{user}")
    public String getUserAccounts(@AuthenticationPrincipal UserDetails userDetails, @PathVariable User user, Model model) {
        if (!user.getEmail().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(Role.ADMIN)) {
            return "accessDenied";
        }
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/{user}/change_email")
    public String changeEmail(@PathVariable User user, @RequestParam String email) {
        if (userService.changeEmail(user, email)) {
            if (user.getRole().equals(Role.ADMIN)) {
                return "redirect:/user/" + user.getId() + "?message=updated&warn=sign_in_email";
            }
            return "redirect:/wallet?message=updated&warn=sign_in_email";
        }
        return "redirect:/user/" + user.getId() + "?warn=not_updated";
    }

    @PostMapping("/{user}/change_password")
    public String changeEmail(@PathVariable User user, @RequestParam(name = "old_password") String oldPassword, @RequestParam String password) {
        if (userService.changePassword(user, oldPassword, password)) {
            return "redirect:/user/" + user.getId() + "?message=updated";
        }
        return "redirect:/user/" + user.getId() + "?warn=not_updated";
    }
}
