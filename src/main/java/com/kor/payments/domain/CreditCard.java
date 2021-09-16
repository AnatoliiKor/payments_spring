package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_number_generator")
    private long id;
    @Column(unique = true)
    private long cardNumber;

    @OneToOne(mappedBy = "card")
    private Account account;


    //    public String getCardNumberSpaces() {
//        String number = String.valueOf(this.cardNumber);
//        if (number == null) return null;
//        char delimiter = ' ';
//        return number.replaceAll(".{4}(?!$)", "$0" + delimiter);
//    }

}
