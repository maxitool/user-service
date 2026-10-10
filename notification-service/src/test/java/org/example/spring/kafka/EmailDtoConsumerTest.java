package org.example.spring.kafka;

import org.example.kafka.Operation;
import org.example.kafka.dto.EmailDto;
import org.example.spring.services.NotificationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static org.example.kafka.CommunicationData.RECORD_HEADER_OPERATION_KEY;
import static org.example.kafka.CommunicationData.TOPIC;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = TOPIC
)
class EmailDtoConsumerTest {
    private static final int VERIFY_TIMEOUT_MILLIS = 5000;
    private static ObjectMapper mapper;

    @Autowired
    private KafkaTemplate<String, EmailDto> kafkaTemplate;

    @MockitoBean
    private NotificationService notificationService;

    private EmailDto dto;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    void setUpDto() {
        dto = new EmailDto("test@mail.ru");
    }

    @Test
    void when_createMessageSent_then_consumerCallsSendUserCreatedToEmail() {
        byte[] operation = Operation.CREATE.getValue().getBytes(StandardCharsets.UTF_8);

        kafkaTemplate.send(MessageBuilder
                .withPayload(mapper.writeValueAsString(dto))
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(RECORD_HEADER_OPERATION_KEY, operation)
                .build());

        verify(notificationService, timeout(VERIFY_TIMEOUT_MILLIS))
                .sendUserCreatedToEmail(dto);
    }

    @Test
    void when_deleteMessageSent_then_consumerCallsSendUserDeletedToEmail() {
        byte[] operation = Operation.DELETE.getValue().getBytes(StandardCharsets.UTF_8);

        kafkaTemplate.send(MessageBuilder
                .withPayload(mapper.writeValueAsString(dto))
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(RECORD_HEADER_OPERATION_KEY, operation)
                .build());

        verify(notificationService, timeout(VERIFY_TIMEOUT_MILLIS))
                .sendUserDeletedToEmail(dto);
    }
}
