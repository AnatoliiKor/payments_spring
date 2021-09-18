package com.kor.payments.services;

import com.kor.payments.domain.CreditCard;
import com.kor.payments.repository.CreditCardRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardServiceTest {
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private CreditCardRepository creditCardRepository;


    @Test
    public void getNewCard() {
        CreditCard card = creditCardService.getNewCard(1000L);
        assertEquals(2604000000001000L, card.getCardNumber());
        assertNotNull(card.getId());
        creditCardRepository.delete(card);
    }
}