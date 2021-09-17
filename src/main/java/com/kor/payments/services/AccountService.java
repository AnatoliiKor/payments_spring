package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.User;
import com.kor.payments.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private static final Logger log = LogManager.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;


    public List<Account> findAllAccounts () {
        return accountRepository.findAll();
    }

    public boolean newAccount(String accountName, String currency, User user) {
        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(Currency.valueOf(currency));
        account.setUser(user);
        accountRepository.save(account);
        log.info("Account {} is added to DB", accountName);
        return true;
    }


}
