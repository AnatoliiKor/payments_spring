package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.AccountRequest;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.User;
import com.kor.payments.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class AccountService {
    private static final Logger log = LogManager.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private AccountRequestService accountRequestService;


    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAllPage(int page, String sort, String order) {
        return accountRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }

    @Transactional(rollbackFor = {SQLException.class})
    public boolean newAccount(String accountName, String currency, User user) {
        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(Currency.valueOf(currency));
        account.setUser(user);
        account = accountRepository.save(account);
        account.setCard(creditCardService.getNewCard(account.getId()));
        accountRepository.save(account);
        accountRequestService.newUnblockAccountRequest(account);
        return true;
    }

    public Account findById(long accountId) {
        return accountRepository.findById(accountId);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public boolean setIsActive(Account account, boolean isActive) {
        account.setActive(isActive);
        if (accountRepository.save(account).isActive() == isActive) {
            if (accountRequestService.removeRequest(account)) {
                log.info("Action is changed to {} for account {}", isActive, account.getId());
                return true;
            } else {
                log.info("Action is changed to {} for account {} but request {} is not deleted", isActive,
                        account.getId(), account.getAccountRequest().getId());
                return false;
            }
        } else {
            log.info("Action is not changed to {} for account {}", isActive, account.getId());
            return false;
        }
    }

    public Account findAccountByAccountName(String accountName) {
        return accountRepository.findAccountByAccountName(accountName);
    }

    public List<Account> findAccountsByUser(User user) {
        return accountRepository.findAccountsByUser(user);
    }
}
