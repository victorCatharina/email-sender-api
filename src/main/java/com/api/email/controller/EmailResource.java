package com.api.email.controller;

import com.api.email.dto.SendEmailRequest;
import com.api.email.service.EmailSenderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailResource {

    private final EmailSenderService emailSenderService;

    public EmailResource(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping(path = "send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(@RequestBody SendEmailRequest emailRequest) {

        emailSenderService.send(emailRequest);
        return ResponseEntity.ok("Email Enviado com Sucesso!");
    }

}
