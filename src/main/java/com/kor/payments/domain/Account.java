package com.kor.payments.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "number_generator")
    @SequenceGenerator(name="number_generator", sequenceName = "number_seq", allocationSize = 1, initialValue = 1000000000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @PositiveOrZero
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


    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }

}
