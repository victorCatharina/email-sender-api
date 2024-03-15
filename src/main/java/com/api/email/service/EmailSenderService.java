package com.api.email.service;

import com.api.email.dto.SendEmailRequest;
import com.api.email.entity.ParamEmailEntity;
import com.api.email.repository.ParamEmailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailSenderService {

    private final ParamEmailRepository paramEmailRepository;

    public EmailSenderService(ParamEmailRepository paramEmailRepository) {
        this.paramEmailRepository = paramEmailRepository;
    }

    public void send(SendEmailRequest emailRequest) {

        ParamEmailEntity paramEmail;

        if (emailRequest.emailId() != null && emailRequest.emailId() > 0) {
            paramEmail = paramEmailRepository.findById(emailRequest.emailId())
                    .orElseThrow(() -> new RuntimeException("Email informed not found on database"));

            if (!paramEmail.getIsActive())
                throw new RuntimeException("Email informed is not active on database");
        } else {
            paramEmail = paramEmailRepository.findActiveMainEmail()
                    .orElseThrow(() -> new RuntimeException("None main email was found"));
        }

        Properties properties = new Properties();

        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        properties.put("mail.smtp.host", paramEmail.getMailHost());
        properties.put("mail.smtp.socketFactory.port", paramEmail.getPort());
        properties.put("mail.smtp.auth", paramEmail.getUseAuth());
        properties.put("mail.smtp.port", paramEmail.getPort());

        final String mailUser = paramEmail.getMailUser();
        final String password = paramEmail.getPassword();

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, password);
            }
        });

        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUser));

            Address[] toUser = InternetAddress.parse(emailRequest.recipients());

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(emailRequest.subject());
            message.setText(emailRequest.message());

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
