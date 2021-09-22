package com.kor.payments.services;

import com.kor.payments.domain.Currency;
import com.kor.payments.domain.CurrencyRate;
import com.kor.payments.repository.CurrencyRateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyRateService {
    private static final Logger log = LogManager.getLogger(CurrencyRateService.class);
    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    public List<CurrencyRate> findAllSorted() {
        return currencyRateRepository.findAll(Sort.by(Sort.Direction.DESC, "currency"));
    }

    public boolean update(CurrencyRate currencyRate, double rate) {
        currencyRate.setExchangeRate(rate);
        currencyRate.setUpdated(LocalDateTime.now());
        currencyRateRepository.save(currencyRate);
        return true;
    }

    public int doExchange(Currency payer, Currency receiver, int amountForReceiver) {
        double payerRate;
        double receiverRate;
        if (payer.equals(Currency.UAH)) {
            payerRate = 1;
        } else {
            payerRate = getExchangeRateByCurrency(payer);
        }
        if (receiver.equals(Currency.UAH)) {
            receiverRate = 1;
        } else {
            receiverRate = getExchangeRateByCurrency(receiver);
        }
        int amountForPayer = (int) (amountForReceiver*receiverRate/payerRate);
        return amountForPayer;
    }

    public double getExchangeRateByCurrency (Currency currency) {
        return currencyRateRepository.findByCurrency(currency).getExchangeRate();
    }
}
