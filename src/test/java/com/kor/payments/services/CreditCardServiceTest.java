package com.kor.payments.services;

import com.kor.payments.domain.CreditCard;
import com.kor.payments.repository.CreditCardRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardServiceTest {
    @Autowired
    private CreditCardService creditCardService;
    @MockBean
    private CreditCardRepository creditCardRepository;


    @Test
    public void getNewCard() {
        CreditCard card = creditCardService.getNewCard(1L);
        assertEquals(2604000000000001L, card.getCardNumber());
        Mockito.verify(creditCardRepository, Mockito.times(1)).save(card);
    }
}