package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.CreditCard;
import com.kor.payments.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private CreditCardService creditCardService;
    @MockBean
    private AccountRequestService accountRequestService;


    @Test
    public void newAccount() {
        Account account = new Account();
        account.setId(1L);
        Mockito.doReturn(account)
                .when(accountRepository)
                .save(account);
        Mockito.doReturn(new CreditCard())
                .when(creditCardService)
                .getNewCard(account.getId());
        boolean isCreated = accountService.newAccount(account);
        Assert.assertTrue(isCreated);
        Mockito.verify(accountRepository, Mockito.times(2)).save(account);
        Mockito.verify(creditCardService, Mockito.times(1)).getNewCard(account.getId());
        Mockito.verify(accountRequestService, Mockito.times(1)).newAccountRequest(account, "UNBLOCK");
    }

    @Test
    public void setIsActive() {
        Account account = new Account();
        Account accountIsActive = new Account();
        accountIsActive.setActive(true);
        Mockito.doReturn(accountIsActive)
                .when(accountRepository)
                .save(account);
        boolean isActiveChenged = accountService.setIsActive(account, true);
        Assert.assertTrue(isActiveChenged);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test
    public void refill() {
        Account account = new Account();
        account.setBalance(1000L);
        int amount = 100;
        Account accountRefilled = new Account();
        accountRefilled.setBalance(1100L);
        Mockito.doReturn(accountRefilled)
                .when(accountRepository)
                .save(account);
        boolean isRefilled = accountService.refill(account, amount);
        Assert.assertTrue(isRefilled);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }

    @Test
    public void refillFailed() {
        Account account = new Account();
        account.setBalance(1000L);
        int amount = 100;
        boolean isRefilled = accountService.refill(account, amount);
        Assert.assertFalse(isRefilled);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
    }
}