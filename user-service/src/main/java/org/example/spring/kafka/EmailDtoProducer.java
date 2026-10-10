package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.Operation;
import org.example.kafka.dto.EmailDto;
import org.example.kafka.dto.response.MetaApiResponse;
import org.example.spring.feign.NotificationServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.example.kafka.CommunicationData.KAFKA_SEND_TIMEOUT;
import static org.example.kafka.CommunicationData.RECORD_HEADER_OPERATION_KEY;
import static org.example.kafka.CommunicationData.TOPIC;


@Component
@Slf4j
public class EmailDtoProducer {

    private final KafkaTemplate<String, EmailDto> kafkaTemplate;
    private final Map<
            Operation,
            Function<EmailDto, ResponseEntity<MetaApiResponse<Void>>>> sendViaFeignMethods;

    public EmailDtoProducer(KafkaTemplate<String, EmailDto> kafkaTemplate,
                            NotificationServiceClient notificationServiceClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.sendViaFeignMethods = Map.of(
                Operation.CREATE, notificationServiceClient::sendUserCreatedToEmail,
                Operation.DELETE, notificationServiceClient::sendUserDeletedToEmail
        );
    }

    public void sendToEmailUserCreated(String email) {
        send(Operation.CREATE, new EmailDto(email));
    }

    public void sendToEmailUserDeleted(String email) {
        send(Operation.DELETE, new EmailDto(email));
    }

    protected void send(Operation operation, EmailDto emailDto) {
        kafkaTemplate.send(createKafkaEvent(operation, emailDto))
                .orTimeout(KAFKA_SEND_TIMEOUT, TimeUnit.SECONDS)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka not responding for {}. Reason: {}", emailDto, ex.getMessage());
                        if (!sendViaFeignMethods.containsKey(operation)) {
                            log.error("can't recognize operation, allow:{}", sendViaFeignMethods.keySet());
                            return;
                        }
                        sendViaFeignMethods.get(operation).apply(emailDto);
                    } else {
                        log.info("Sent to partition {} offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    protected ProducerRecord<String, EmailDto> createKafkaEvent(Operation operation, EmailDto emailDto) {
        ProducerRecord<String, EmailDto> producerRecord =
                new ProducerRecord<>(TOPIC, emailDto.email(), emailDto);
        producerRecord.headers().add(new RecordHeader(RECORD_HEADER_OPERATION_KEY,
                operation.name().getBytes(StandardCharsets.UTF_8)));
        return producerRecord;
    }
}
