package com.example.service;


import com.example.dto.EmailRequest;
import com.example.model.EmailLog;
import com.example.Repository.EmailLogRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailLogRepository emailLogRepository;

    public void sendEmail(EmailRequest request) {
        for (String recipient : request.getTo()) {
            EmailLog log = new EmailLog();
            log.setRecipient(recipient);
            log.setSubject(request.getSubject());
            log.setBody(request.getBody());
            log.setSentAt(LocalDateTime.now());

            try {
                var mimeMessage = mailSender.createMimeMessage();
                var helper = new MimeMessageHelper(mimeMessage, false);
                helper.setTo(recipient);
                helper.setSubject(request.getSubject());
                helper.setText(request.getBody(), request.isHtml());
                mailSender.send(mimeMessage);

                log.setStatus("SUCCESS");
            } catch (MessagingException e) {
                log.setStatus("FAILED");
                log.setErrorMessage(e.getMessage());
            }

            emailLogRepository.save(log);
        }
    }
}
