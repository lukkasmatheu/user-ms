package com.vm.user.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    void testSendEmail(){
        emailService.sendEmail("Teste envio email", "email@email.com");
    }

}
