package org.example.spring.services;

import org.example.spring.dto.notification_service.NotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(
            topics = "ui-notifications",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )

    public void listenNotifications (NotificationDto notification) {
        System.out.println("=================================================");
        System.out.println("🔔 ИНФОРМАЦИЯ ДЛЯ СЕРВИСА УВЕДОМЛЕНИЙ (RECORD):");
        System.out.println("Кому отправить (ID): " + notification.userEmail());
        System.out.println("Тип отправки: " + notification.operationType());
        System.out.println("Текст сообщения: " + notification.message());
        System.out.println("=================================================");
    }
}
