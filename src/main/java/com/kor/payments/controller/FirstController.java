package com.kor.payments.controller;

import com.kor.payments.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class FirstController {

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name="name", required=false, defaultValue="World2") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        model.addAttribute("last_name", name);
        return "index";
    }

    @GetMapping
    public String main (@AuthenticationPrincipal UserDetails user, HttpSession session, Model model) {
//        User user1 = (User) user;
        session.setAttribute("user_auth", user);
//        User user2 = (User) session.getAttribute("user_auth");
//        model.addAttribute("user_auth", user1);
        return "index";
    }

}
