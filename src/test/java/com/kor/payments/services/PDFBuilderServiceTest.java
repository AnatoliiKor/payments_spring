package com.kor.payments.services;

import com.itextpdf.text.DocumentException;
import com.kor.payments.domain.Account;
import com.kor.payments.domain.Currency;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PDFBuilderServiceTest {
    private static final Logger log = LogManager.getLogger(PDFBuilderServiceTest.class);

    @Autowired
    PDFBuilderService pdfBuilderService;

    @Test
    public void getCheckPassed() {
        Transaction payment = new Transaction();
        Account payer = new Account();
        payer.setUser(new User());
        Account receiver = new Account();
        receiver.setCurrency(Currency.UAH);
        receiver.setUser(new User());
        payment.setReceiver(receiver);
        payment.setCurrency(Currency.UAH);
        payment.setPayer(payer);
        boolean isCreated = false;
        try {
            isCreated = pdfBuilderService.getCheck(payment);
        } catch (IOException | DocumentException | URISyntaxException e) {
            log.debug("Test Check is not created due to {}", e.getMessage());
        }
        Assert.assertTrue(isCreated);
    }
}