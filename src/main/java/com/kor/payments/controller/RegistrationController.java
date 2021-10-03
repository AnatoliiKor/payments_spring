package com.kor.payments.controller;

import com.kor.payments.constants.Constant;
import com.kor.payments.utils.ControllerUtils;
import com.kor.payments.domain.User;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;


@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute(Constant.USER, new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute(Constant.USER, user);
            return "registration";
        } else {
            if (!userService.addUser(user)) {
                model.addAttribute(Constant.WARN, "registration_user_exist");
                return "registration";
            }
        }
        return "redirect:/login";
    }
}
