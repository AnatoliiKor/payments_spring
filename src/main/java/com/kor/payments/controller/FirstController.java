package com.kor.payments.controller;

import com.kor.payments.constants.Constant;
import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class FirstController {

    @GetMapping
    public String main(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        session.setAttribute(Constant.USER_AUTH, userDetails);
        User user = (User) userDetails;
        if (user.getRole().equals(Role.ADMIN)) {
            return "redirect:admin";
        } else {
            return "redirect:wallet";
        }
    }
}
