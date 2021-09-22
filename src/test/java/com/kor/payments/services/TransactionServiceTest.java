package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import com.kor.payments.repository.AccountRepository;
import com.kor.payments.repository.TransactionRepository;
import com.kor.payments.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionService transactionService;
    User user;

    @Before
    public void newUser () {
        user = new User();
        user.setId(2L);
    }

    @Test
    public void findAllPage() {
        List<Transaction> transactions = transactionService.findAllPage(1, "amount", "ASC");
        assertEquals(10, transactions.size());
    }

    @Test
    public void findPayerTransactions() {
        List<Transaction> transactions =  transactionService.findPayerTransactions(user);
        assertTrue(transactions.size() != 0);
    }

    @Test
    public void findReceiverTransactions() {
        List<Transaction> transactions =  transactionService.findReceiverTransactions(user);
        assertTrue(transactions.size() != 0);
    }

    @Test
    public void Last() {
        Transaction transaction = transactionRepository.findTopByCurrencyByIdDesc(Currency.USD);
        Assert.assertEquals(Currency.USD, transaction.getCurrency());
    }

}