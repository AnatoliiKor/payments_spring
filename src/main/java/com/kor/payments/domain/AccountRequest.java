package com.kor.payments.domain;

import javax.persistence.*;

@Entity
public class AccountRequest {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "account")
    private Account account;

    @Enumerated(EnumType.STRING)
    private Action action;

    public enum Action {
        BLOCK,
        UNBLOCK
    }
}
