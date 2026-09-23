package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.Operation;
import org.example.SendToEmailCreateDeleteDto;
import org.example.spring.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
public class SendToEmailCreateDeleteDtoConsumer {
    private final Map<Operation, Consumer<String>> methods;

    public SendToEmailCreateDeleteDtoConsumer(NotificationService notificationService) {
        if (notificationService == null) {
            log.error("notificationService is null.");
            this.methods = Map.of();
            return;
        }
        this.methods = Map.of(
                Operation.CREATE, notificationService::sendUserCreatedToEmail,
                Operation.DELETE, notificationService::sendUserDeletedToEmail
        );
    }

    @KafkaListener(topics = "send-to-email-create-delete-dto", groupId = "all-users-group")
    public void handleSendToEmailCreateDeleteDto(
            @Payload SendToEmailCreateDeleteDto dto,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received {} for {} (partition {}, offset {})",
                dto.operation(), dto.email(), partition, offset);
        if (!methods.containsKey(dto.operation())) {
            log.error("Can't recognize the received operation.");
            return;
        }
        methods.get(dto.operation()).accept(dto.email());
    }
}
