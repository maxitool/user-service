package org.example.spring.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.EmailMessages;
import org.example.constants.ResourcesUrl;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        if (mailSender == null) {
            log.error("mailSender is null.");
        }
        this.mailSender = mailSender;
    }

    public void sendUserCreatedToEmail(String toEmail) {
        send(toEmail, EmailMessages.USER_CREATED_SUBJECT,
                String.format(EmailMessages.USER_CREATED_TEXT, ResourcesUrl.CREATED_IMAGE_URL));
    }

    public void sendUserDeletedToEmail(String toEmail) {
        send(toEmail, EmailMessages.USER_DELETED_SUBJECT,
                String.format(EmailMessages.USER_DELETED_TEXT, ResourcesUrl.DELETED_IMAGE_URL));
    }

    private void send(String toEmail, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
            log.info("Message was sent to {} successfully.", toEmail);
        } catch (MessagingException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
