package com.kor.payments.services;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.Transaction;
import com.kor.payments.repository.AccountRepository;
import com.kor.payments.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


   public int doCurrencyExchange(int amount, Currency available, Currency necessary) {
//       int available = int getExchangeRateByCurrency (available);
//       int necessary = getExchangeRateByCurrency (necessary);
       return 0;
   }
    @Transactional
    public boolean makeTransaction(Transaction payment) {
        Account payer = payment.getPayer();
        payer.setBalance(payer.getBalance()-payment.getAmount());
        accountRepository.save(payer);
        Account receiver = payment.getReceiver();
        receiver.setBalance(receiver.getBalance()+payment.getAmount());
        accountRepository.save(receiver);
        transactionRepository.save(payment);
        return true;
    }
}
