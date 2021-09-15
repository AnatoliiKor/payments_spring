package com.kor.payments.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private int exchangeRate;
    private LocalDateTime updated = LocalDateTime.now();

}
