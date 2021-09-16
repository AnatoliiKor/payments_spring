package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private int exchangeRate;
    private LocalDateTime updated = LocalDateTime.now();

}
