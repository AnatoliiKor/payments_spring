package com.kor.payments.controller;

import com.kor.payments.repository.UserRepository;
import com.kor.payments.services.AccountService;
import com.kor.payments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;


}
