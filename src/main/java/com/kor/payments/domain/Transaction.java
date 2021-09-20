package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Account payer;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Account receiver;
    private LocalDateTime registered = LocalDateTime.now();
    private String destination;
    private int amount;
    private int balanceAfter;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }
}
