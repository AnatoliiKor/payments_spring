package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "number_generator")
    @SequenceGenerator(name = "number_generator", sequenceName = "number_seq", allocationSize = 1, initialValue = 1000000000)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @PositiveOrZero
    private long balance;
    @NotBlank(message = "Cannot be empty")
    @Length(max = 30, message = "Too long (more then 30 characters)")
    private String accountName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "account", fetch = FetchType.EAGER)
    private AccountRequest accountRequest;

    private LocalDateTime registered = LocalDateTime.now();
    private boolean active;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "card")
    private CreditCard card;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY)
    private List<Transaction> payments;


//    public String getFormatedDate() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
//        return this.registered.format(formatter);
//    }

}
