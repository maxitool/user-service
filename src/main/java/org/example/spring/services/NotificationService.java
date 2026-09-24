package org.example.spring.services;

import io.swagger.v3.oas.annotations.headers.Header;
import org.example.spring.dto.notification_service.NotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(
            topics = "ui-notification",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )

    public void listenNotifications(
            NotificationDto notification) {

    }
}
//@KafkaListener(topics = "ui-notification", groupId = "notification-group")
//public void listenNotifications(
//        NotificationDto notification,
//        @Header("user-email") String userEmail,
//        // Достаем автоматически сгенерированное брокером время!
//        @Header(org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp
//)