package org.example.spring.kafka;

import org.example.spring.dto.notification_service.NotificationDto;
import org.springframework.boot.CommandLineRunner;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTestService implements CommandLineRunner {

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    // Внедряем шаблон для отправки сообщений
    public KafkaTestService(KafkaTemplate<String, NotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("🚀 Авто-отправка тестового рекорда при старте...");

        // 2. Создаем объект нашего рекорда
        NotificationDto notification = new NotificationDto(
                "user_irina_777",
                "Тестовое уведомление через JSON Record успешно доставлено!",
                "EMAIL"
        );

        // 3. Отправляем объект в нужный топик
        kafkaTemplate.send("ui-notifications", "test_key", notification);

    }
}
