package com.kor.payments.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "greeting";
    }

    @GetMapping
    public String main (Model model) {
        return "main";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("name", "TOXA");
        model.addAttribute("last_name", "KOR");
        model.addAttribute("warn", "name");
        model.addAttribute("phone_number", "1111ff1111");
        return "registration";
    }

}
