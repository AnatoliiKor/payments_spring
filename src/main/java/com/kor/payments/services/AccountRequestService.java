package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.AccountRequest;
import com.kor.payments.repository.AccountRequestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountRequestService {
    private static final Logger log = LogManager.getLogger(AccountRequestService.class);
    @Autowired
    private AccountRequestRepository accountRequestRepository;

    public boolean newAccountRequest(Account account, String request) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccount(account);
        accountRequest.setAction(AccountRequest.Action.valueOf(request));
        accountRequestRepository.save(accountRequest);
        log.info("New request {} for account {} is created", request, account.getId());
        return true;
    }

    public List<AccountRequest> findAll() {
        return accountRequestRepository.findAll();
    }

    public boolean removeRequest(Account account) {
        AccountRequest accountRequest = account.getAccountRequest();
        accountRequestRepository.delete(accountRequest);
        return !accountRequestRepository.findById(accountRequest.getId()).isPresent();
    }
}
