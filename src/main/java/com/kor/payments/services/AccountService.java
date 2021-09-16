package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    public List<Account> findAllAccounts () {
        return accountRepository.findAll();

    }

}
