package com.kor.payments.services;

import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    private String email = "user@test.ua";
    private String date = "test";

    @Test
    public void addAndFindUser() {
        User user = new User();
        user.setPassword(date);
        user.setName(date);
        user.setLastName(date);
        user.setEmail(email);
        user.setPhoneNumber(2121212121L);
        assertTrue(userService.addUser(user));
        user = userService.findUserByEmail(email);
        userRepository.delete(user);
        assertNull(userService.findUserByEmail(email));
    }
}