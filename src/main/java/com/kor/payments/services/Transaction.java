package com.kor.payments.services;

import com.kor.payments.domain.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Transaction {

   public int doCurrencyExchange(int amount, Currency available, Currency necessary) {
//       int available = int getExchangeRateByCurrency (available);
//       int necessary = getExchangeRateByCurrency (necessary);
       return 0;
   }
}
