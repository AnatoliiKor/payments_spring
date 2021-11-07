package com.kor.payments.services;

import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceDbTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
    @Test
    public void createUser() {
        user = new User();
        user.setName("Semen");
        user.setLastName("Semenov");
        user.setMiddleName("Semenovich");
        user.setPassword("1");
        user.setEmail("semen@google.com");
        user.setPhoneNumber(2222222222L);
    }

    @After
    @Test
    public void deleteUser() {
        userRepository.delete(user);
    }

    @Test
    public void addUser() {
        boolean isUserCreated = userService.addUser(user);
        Assert.assertTrue(isUserCreated);
        User userFromDb = userService.findUserByEmail("semen@google.com");
        Assert.assertNotNull(userFromDb);
        Assert.assertTrue(userFromDb.isActive());
        Assert.assertEquals(Role.CLIENT, userFromDb.getRole());
    }

}