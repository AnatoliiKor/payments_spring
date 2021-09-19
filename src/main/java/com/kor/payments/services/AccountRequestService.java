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

    public boolean newUnblockAccountRequest(Account account) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccount(account);
        accountRequest.setAction(AccountRequest.Action.BLOCK);
        accountRequestRepository.save(accountRequest);
        log.info("New request UNBLOCK for account {} is created", account.getId());
        return true;
    }

    public List<AccountRequest> findAll() {
        return accountRequestRepository.findAll();
    }

    public boolean removeRequest(Account account) {
        AccountRequest accountRequest = account.getAccountRequest();
        accountRequestRepository.delete(accountRequest);
        if (accountRequestRepository.findById(accountRequest.getId()) != null) {
            return true;
        } else {
            return false;
        }
    }
}
