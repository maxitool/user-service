package org.example;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.spring.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void when_sendUserCreatedToEmail_then_success() {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.CREATE, "test@mail.ru");
        MimeMessage message = new MimeMessage((jakarta.mail.Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        assertDoesNotThrow(() -> notificationService.sendUserCreatedToEmail(dto));
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    void when_sendUserCreatedToEmail_then_errorMailException () {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.CREATE, "test@mail.ru");
        MimeMessage mimeMessage = new MimeMessage((jakarta.mail.Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("SMTP server connection failed"))
                .when(javaMailSender).send(any(MimeMessage.class));
        assertDoesNotThrow(() -> notificationService.sendUserCreatedToEmail(dto));
    }

    @Test
    void when_sendUserCreatedToEmail_then_errorMessagingException () throws MessagingException {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.CREATE, "test@mail.ru");
        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
        doThrow(new MessagingException("Error JavaMail API"))
                .when(mimeMessageMock).setSubject(anyString(), anyString());
        assertDoesNotThrow(() -> notificationService.sendUserCreatedToEmail(dto));
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void when_sendUserDeletedToEmail_then_success() {

        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.DELETE, "test@mail.ru");
        MimeMessage message = new MimeMessage((jakarta.mail.Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        assertDoesNotThrow(() -> notificationService.sendUserDeletedToEmail(dto));
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    void when_sendUserDeletedToEmail_then_errorMailException (){
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.DELETE, "test@mail.ru");
        MimeMessage mimeMessage = new MimeMessage((jakarta.mail.Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("SMTP server connection failed"))
                .when(javaMailSender).send(any(MimeMessage.class));
        assertDoesNotThrow(() -> notificationService.sendUserDeletedToEmail(dto));
    }

    @Test
    void when_sendUserDeletedToEmail_then_errorMessagingException() throws MessagingException {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.DELETE, "test@mail.ru");
        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
        doThrow(new MessagingException("Error JavaMail API"))
                .when(mimeMessageMock).setSubject(anyString(), anyString());
        assertDoesNotThrow(() -> notificationService.sendUserCreatedToEmail(dto));
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }
}
