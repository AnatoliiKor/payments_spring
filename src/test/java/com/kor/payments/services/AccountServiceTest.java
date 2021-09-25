package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.AccountRepository;
import com.kor.payments.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    private String test = "test";
    User user;
    Account account;

    @Before
    public void addUser(){
        user = new User();
        user.setPassword(test);
        user.setName(test);
        user.setLastName(test);
        user.setEmail("account@test.ua");
        user.setPhoneNumber(2121212122L);
        userService.addUser(user);
        user = userService.findUserByEmail("account@test.ua");
    }

    @After
    public void deleteUser() {
        userRepository.delete(user);
    }

//    @Test
//    public void findAllAccounts() {
//        List<Account> accounts = accountService.findAllAccounts();
//        assertNotNull(accounts);
//        assertNotEquals(1L, accounts.size());
//    }

    @Test
    public void newAccount() {
        Account account = new Account();
        account.setUser(user);
        account.setAccountName(test);
        account.setCurrency(Currency.valueOf("USD"));
        assertTrue(accountService.newAccount(account));
        List<Account> accounts = accountService.findAccountsByUser(user);
        assertEquals(1, accounts.size());
        account = accounts.get(0);
        accountRepository.delete(account);
    }
}