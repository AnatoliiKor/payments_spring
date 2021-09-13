package com.kor.payments.domain;

import javax.persistence.*;

@Entity
@Table
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_number_generator")
    @SequenceGenerator(name="card_number_generator", sequenceName = "card_number_seq")
    private Long id;

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
