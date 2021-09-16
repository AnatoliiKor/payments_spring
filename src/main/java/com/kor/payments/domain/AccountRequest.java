package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
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
