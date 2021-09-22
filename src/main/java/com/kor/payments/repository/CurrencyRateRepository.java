package com.kor.payments.repository;

import com.kor.payments.domain.Currency;
import com.kor.payments.domain.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    CurrencyRate findByCurrency(Currency currency);
}
