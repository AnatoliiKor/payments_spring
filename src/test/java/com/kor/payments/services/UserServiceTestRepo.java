package com.kor.payments.services;

import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTestRepo {
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
        user.setMiddleName(date);
        user.setLastName(date);
        user.setEmail(email);
        user.setPhoneNumber(2121212121L);
        assertTrue(userService.addUser(user));
        user = userService.findUserByEmail(email);
        assertEquals(email, user.getEmail());
        userService.setIsActive(user, false);
        assertFalse(userService.findById(user.getId()).isActive());
        userRepository.delete(user);
        assertNull(userService.findUserByEmail(email));
    }

    @Test
    public void findAllPage() {
        List<User> users = userService.findAllPage(0, "email", "DESC");
        assertEquals(10, users.size());
    }
}