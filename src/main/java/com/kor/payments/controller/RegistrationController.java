package com.kor.payments.controller;

import com.kor.payments.domain.User;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;


@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

//    @PostMapping("/registration")
//    public String addUser(
//            Model model) {
//        if (bindingResult.hasErrors()) {
//            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
//            model.mergeAttributes(errorMap);
//            model.addAttribute("user", user);
//            return "registration";
//        } else {
//            if (!userService.addUser(user)) {
//                model.addAttribute("usernameError", "User exists! Try another User name");
//                return "registration";
//            }
//        }
//        return "redirect:login";
//    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
       if (!userService.addUser(user)) {
           model.addAttribute("warn", "registration_user_exist");
           return "registration";
       }
       return "redirect:login";
    }

}
