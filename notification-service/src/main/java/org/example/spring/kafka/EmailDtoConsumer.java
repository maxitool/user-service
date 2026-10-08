package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.Operation;
import org.example.kafka.dto.EmailDto;
import org.example.spring.services.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import static org.example.kafka.CommunicationData.RECORD_HEADER_OPERATION_KEY;
import static org.example.kafka.CommunicationData.TOPIC;

@Component
@Slf4j
public class EmailDtoConsumer {

    private final Map<Operation, Consumer<EmailDto>> methods;

    public EmailDtoConsumer(NotificationService notificationService) {
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

    @KafkaListener(topics = TOPIC, groupId = "all-users-group")
    public void handleSendToEmailCreateDeleteDto(
            @Payload EmailDto dto,
            @Header(value = RECORD_HEADER_OPERATION_KEY) byte[] operationString,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received {} for {} (partition {}, offset {})",
                operationString, dto.email(), partition, offset);
        Operation operation = Operation.fromString(new String(operationString, StandardCharsets.UTF_8));
        log.debug("Received correct operation {}.", operation);
        if (!methods.containsKey(operation)) {
            log.error("Can't recognize the received operation {}.", operation);
            return;
        }
        methods.get(operation).accept(dto);
    }
}
