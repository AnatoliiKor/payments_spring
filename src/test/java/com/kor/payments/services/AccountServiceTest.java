package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.AccountRepository;
import com.kor.payments.repository.UserRepository;
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
        accountService.newAccount(test, "USD", user);
        account = accountRepository.findAccountByAccountName("test");
    }

    @After
    public void deleteUser() {
        accountRepository.delete(account);
        userRepository.delete(user);
    }

    @Test
    public void findAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();
        assertNotNull(accounts);
        assertNotEquals(1L, accounts.size());
    }

    @Test
    public void findAccountByAccountName() {
        assertEquals("test", accountService.findAccountByAccountName("test").getAccountName());
    }

    @Test
    public void findAccountsByUser() {
        assertEquals(1, accountService.findAccountsByUser(user).size());
    }
    @Test
    public void newAccount() {
        assertTrue(accountService.newAccount("test new account", "USD", user));
        Account newAccount = accountService.findAccountByAccountName("test new account");
        assertEquals("test new account", newAccount.getAccountName());
        accountRepository.delete(newAccount);
    }
}