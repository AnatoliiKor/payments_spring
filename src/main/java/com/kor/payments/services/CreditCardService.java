package com.kor.payments.services;

import com.kor.payments.domain.CreditCard;
import com.kor.payments.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCard getNewCard(long accountId) {
        CreditCard card = new CreditCard();
        card.setCardNumber(2604000000000000L + accountId);
        creditCardRepository.save(card);
        return card;
    }
}
