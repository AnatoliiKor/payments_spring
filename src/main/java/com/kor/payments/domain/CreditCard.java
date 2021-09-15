package com.kor.payments.domain;

import javax.persistence.*;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_number_generator")
    private long id;
    @Column(unique = true)
    private long cardNumber;

    @OneToOne(mappedBy = "card")
    private Account account;

    public CreditCard() {
    }


    //    public String getCardNumberSpaces() {
//        String number = String.valueOf(this.cardNumber);
//        if (number == null) return null;
//        char delimiter = ' ';
//        return number.replaceAll(".{4}(?!$)", "$0" + delimiter);
//    }

}
