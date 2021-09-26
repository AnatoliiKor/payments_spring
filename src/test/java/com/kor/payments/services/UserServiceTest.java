package com.kor.payments.services;

import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser() {
        User user = new User();
        boolean isUserCreated = userService.addUser(user);
        Assert.assertTrue(isUserCreated);
        Assert.assertTrue(user.isActive());
        Assert.assertEquals(Role.CLIENT, user.getRole());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void addUserFailedTest(){
        User user = new User();
        user.setEmail("test@test.ua");
        Mockito.doReturn(new User())
                .when(userRepository)
                .findByEmail("test@test.ua");
        boolean isUserCreated = userService.addUser(user);
        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void changeEmailTest(){
        User user = new User();
        String oldEmail = "old@test.ua";
        String newEmail = "new@test.ua";
        user.setEmail(oldEmail);
        User userNewEmail = new User();
        userNewEmail.setEmail(newEmail);
        Mockito.doReturn(userNewEmail)
                .when(userRepository)
                .save(user);
        boolean isEmailChanges = userService.changeEmail(user, newEmail);
        Assert.assertTrue(isEmailChanges);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void changeEmailFailedTest(){
        User user = new User();
        String email = "test@test.ua";
        user.setEmail(email);
        Mockito.doReturn(new User())
                .when(userRepository)
                .findByEmail(email);
        boolean isEmailChanges = userService.changeEmail(user, email);
        Assert.assertFalse(isEmailChanges);
        Mockito.verify(userRepository, Mockito.times(0)).findByEmail(email);
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void changePasswordTest(){
        User user = new User();
        String oldPassword = "1";
        String newPassword = "2";
        Mockito.doReturn(true)
                .when(passwordEncoder)
                .matches(oldPassword, user.getPassword());
        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);
        boolean isPasswordChanged = userService.changePassword(user, oldPassword, newPassword);
        Assert.assertFalse(isPasswordChanged);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

}