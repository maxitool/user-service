package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.EmailDto;
import org.example.kafka.Operation;
import org.example.spring.services.SendEmailService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.example.kafka.CommunicationData.KAFKA_SEND_TIMEOUT;
import static org.example.kafka.CommunicationData.RECORD_HEADER_OPERATION_KEY;
import static org.example.kafka.CommunicationData.TOPIC;


@Component
@Slf4j
public class EmailDtoProducer {


    private final KafkaTemplate<String, EmailDto> kafkaTemplate;

    public EmailDtoProducer(KafkaTemplate<String, EmailDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToEmailUserCreated(String email) {
        send(Operation.CREATE, new EmailDto(email));
    }

    public void sendToEmailUserDeleted(String email) {
        send(Operation.DELETE, new EmailDto(email));
    }

    protected void send(Operation operation, EmailDto emailDto) {
        try {
            var result = kafkaTemplate.send(createKafkaEvent(operation, emailDto))
                    .get(KAFKA_SEND_TIMEOUT, TimeUnit.SECONDS);

            log.info("Sent to partition {} offset {}",
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (Exception e) {
            log.error("Kafka not responding for {}. Reason: {}", emailDto, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected ProducerRecord<String, EmailDto> createKafkaEvent(Operation operation, EmailDto emailDto) {
        ProducerRecord<String, EmailDto> producerRecord =
                new ProducerRecord<>(TOPIC, emailDto.email(), emailDto);
        producerRecord.headers().add(new RecordHeader(RECORD_HEADER_OPERATION_KEY,
                operation.name().getBytes(StandardCharsets.UTF_8)));
        return producerRecord;
    }
}
