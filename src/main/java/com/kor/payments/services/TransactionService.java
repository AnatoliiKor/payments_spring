package com.kor.payments.services;

import com.kor.payments.domain.*;
import com.kor.payments.repository.AccountRepository;
import com.kor.payments.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;


//   public int doCurrencyExchange(int amount, Currency available, Currency necessary) {
////       int available = int getExchangeRateByCurrency (available);
////       int necessary = getExchangeRateByCurrency (necessary);
//       return 0;
//   }

    @Transactional
    public boolean makeTransaction(Transaction payment) {
        Account payer = payment.getPayer();
        payer.setBalance(payer.getBalance()-payment.getAmount());
        accountRepository.save(payer);
        Account receiver = payment.getReceiver();
        receiver.setBalance(receiver.getBalance()+payment.getBalanceAfter());
        accountRepository.save(receiver);
        transactionRepository.save(payment);
        return true;
    }

    public List<Transaction> findAllPage(int page, String sort, String order) {
        return transactionRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }

    public List<Transaction> findPayerTransactionsPage(User user, int page, String sort, String order) {
        return transactionRepository.findByPayer_User(user, PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }

    public List<Transaction> findReceiverTransactionsPage(User user, int page, String sort, String order) {
        return transactionRepository.findByReceiver_User(user, PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }

    public List<Transaction> findPayerTransactions(User user) {
        return transactionRepository.findByPayer_User(user);
    }

    public List<Transaction> findReceiverTransactions(User user) {
        return transactionRepository.findByReceiver_User(user);
    }

}
