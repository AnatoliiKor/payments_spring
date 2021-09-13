package com.kor.payments.domain;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "number_generator")
    @SequenceGenerator(name="number_generator", sequenceName = "number_seq", initialValue = 1000000000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private long balance;
    private String accountName;

//    @Enumerated(EnumType.STRING)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "currency_id", referencedColumnName = "id")
//    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Action action;

//    @Enumerated(EnumType.STRING)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "action_id", referencedColumnName = "id")
//    private Action action;

    private LocalDateTime registered = LocalDateTime.now();
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card", referencedColumnName = "id")
    private CreditCard card;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY)
    private List<Transaction> payment;


    public Account() {
    }


    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }



}
