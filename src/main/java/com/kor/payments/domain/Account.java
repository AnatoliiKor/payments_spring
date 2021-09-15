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
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "number_generator")
    @SequenceGenerator(name="number_generator", sequenceName = "number_seq", initialValue = 1000000000)
    private Long accountNumber;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private long balance;
    private String accountName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
    private AccountRequest accountRequest;

    private LocalDateTime registered = LocalDateTime.now();
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card")
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
