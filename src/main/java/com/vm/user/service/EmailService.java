package com.vm.user.service;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Slf4j
public class EmailService {

    public void sendEmail(String message, String emailDestine){
        log.info("Email enviado para {} com a mensagem: {}", emailDestine, message);
    }
}
