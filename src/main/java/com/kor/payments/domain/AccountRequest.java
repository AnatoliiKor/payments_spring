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
public class AccountRequest {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "account")
    private Account account;

    @Enumerated(EnumType.STRING)
    private Action action;

    private LocalDateTime registered = LocalDateTime.now();

    public enum Action {
        BLOCK,
        UNBLOCK
    }
}
