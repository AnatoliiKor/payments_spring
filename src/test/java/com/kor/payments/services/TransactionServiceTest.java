package com.kor.payments.services;

import com.kor.payments.domain.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @Test
    public void checkReceiver_nullReceiver_returnsReceiverNotFound() {
        String  redirect = transactionService.checkReceiver(100, null);
        Assert.assertTrue(redirect.contains("not_found"));
    }

    @Test
    public void checkReceiver_ReceiverNotActive_returnsReceiverNotFound() {
        Account receiver = new Account();
        receiver.setActive(false);
        String  redirect = transactionService.checkReceiver(100, receiver);
        Assert.assertTrue(redirect.contains("not_found"));
    }

    @Test
    public void checkReceiver_negativeAnount_returnsForm() {
        String  redirect = transactionService.checkReceiver(-100, new Account());
        Assert.assertTrue(redirect.contains("form"));
    }

    @Test
    public void checkReceiver_correctParameters_returnsChecked() {
        String  redirect = transactionService.checkReceiver(100, new Account());
        Assert.assertTrue(redirect.contains("form"));
    }
}