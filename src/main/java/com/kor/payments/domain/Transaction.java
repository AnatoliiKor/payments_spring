package com.kor.payments.domain;

import com.kor.payments.constants.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull(message = Constant.CANNOT_BE_EMPTY)
    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Account payer;
    @NotNull(message = Constant.CANNOT_BE_EMPTY)
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Account receiver;
    private LocalDateTime registered = LocalDateTime.now();
    private String destination;
    @Positive(message = "Must be positive")
    private int amount;
    private int accrual;
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
