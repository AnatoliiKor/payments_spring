package com.kor.payments.controller;

import com.kor.payments.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String main (Model model) {
        return "index";
    }

}
