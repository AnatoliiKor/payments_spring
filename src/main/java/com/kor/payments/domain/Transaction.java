package com.kor.payments.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class Transaction {
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

//    @Enumerated(EnumType.STRING)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "currency_id", referencedColumnName = "id")
//    private Currency currency;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }
}
