package com.kor.payments.services;

import com.kor.payments.domain.Account;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public List<Account> findAllAccountsPagesSorted(List<Account> accounts, int page, String sortBy, String order) {
        long start = page * 10;
        Comparator<Account> comparator = Comparator.comparingLong(Account::getId);
            if("balance".equals(sortBy)) {
                comparator = Comparator.comparingLong(Account::getBalance);
            }
            if("accountName".equals(sortBy)) {
                comparator = Comparator.comparing(Account::getAccountName);
            }
            if("currency".equals(sortBy)) {
                comparator = Comparator.comparing(Account::getCurrency);
            }
            if ("DESC".equals(order)) {
                comparator = comparator.reversed();
            }
        return accounts.stream().sorted(comparator).skip(start).limit(10).collect(Collectors.toList());
    }

    public List<Account> findAllActiveAccountsByUser(User user) {
        return accountRepository.findAccountsByActiveTrueAndUser(user);
    }

    public List<Account> findAllPage(int page, String sort, String order) {
        return accountRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }

    @Transactional(rollbackFor = {SQLException.class})
    public boolean newAccount(Account account) {
        account = accountRepository.save(account);
        account.setCard(creditCardService.getNewCard(account.getId()));
        accountRepository.save(account);
        accountRequestService.newAccountRequest(account, "UNBLOCK");
        return true;
    }

    public Account findByNumberId(long numberId) {
        return accountRepository.findById(numberId);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public boolean setIsActive(Account account, boolean isActive) {
        account.setActive(isActive);
        if (accountRepository.save(account).isActive() == isActive) {
            if (account.getAccountRequest() == null) {
                log.info("Action is changed to {} for account {}", isActive, account.getId());
                return true;
            }
            if (accountRequestService.removeRequest(account)) {
                log.info("Action is changed to {} for account {}", isActive, account.getId());
                return true;
            } else {
                log.debug("Action is changed to {} for account {} but request {} is not deleted", isActive,
                        account.getId(), account.getAccountRequest().getId());
                return false;
            }
        } else {
            log.debug("Action is not changed to {} for account {}", isActive, account.getId());
            return false;
        }
    }

    public List<Account> findAccountsByUser(User user) {
        return accountRepository.findAccountsByUser(user);
    }

    public boolean refill(Account account, int amount) {
        account.setBalance(account.getBalance() + amount);
        Account accountDB = accountRepository.save(account);
        if (accountDB != null && account.getBalance() == accountDB.getBalance()) {
            log.info("Account {} is refilled with {}", account.getId(), amount);
            return true;
        }
        log.debug("Account {} is not refilled with {}", account.getId(), amount);
        return false;
    }
}
