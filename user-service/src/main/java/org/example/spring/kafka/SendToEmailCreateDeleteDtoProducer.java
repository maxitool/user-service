package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.Operation;
import org.example.kafka.EmailDto;
import org.example.spring.services.SendEmailService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.example.kafka.CommunicationData.RECORD_HEADER_KEY;
import static org.example.kafka.CommunicationData.TOPIC;


@Component
@Slf4j
public class SendToEmailCreateDeleteDtoProducer {


    private final KafkaTemplate<String, EmailDto> kafkaTemplate;
    private final SendEmailService sendEmailService;

    public SendToEmailCreateDeleteDtoProducer(KafkaTemplate<String, EmailDto> kafkaTemplate,
                                              SendEmailService sendEmailService) {
        this.kafkaTemplate = kafkaTemplate;
        this.sendEmailService = sendEmailService;
    }

    public void sendToEmailUserCreated(Long id, String email) {
        EmailDto dto = new EmailDto(Operation.CREATE, email);
        CompletableFuture.supplyAsync(() -> {
                    try {
                        return kafkaTemplate.send(TOPIC, id + "id" + dto.hashCode(), dto).get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })

                .orTimeout(7, TimeUnit.SECONDS)
                .thenAccept(result -> {
                    log.info("Sent to partition {} offset {}",
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                })
                .exceptionally(ex -> {
                    log.error("Kafka is not responding. Launching fallback HTTP path for {}. Reason: {}",
                            email, ex.getMessage());

                    sendEmailService.sendViaHttpFallbackCreate(dto);
                    return null;
                });
    }

    public void sendToEmailUserDeleted(Long id, String email) {
        EmailDto dto = new EmailDto(Operation.DELETE, email);

        CompletableFuture.supplyAsync(()-> {
                    try {
                        return kafkaTemplate.send(TOPIC, id + "id" + dto.hashCode(), dto).get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })

                .orTimeout(7, TimeUnit.SECONDS)
                .thenAccept(result -> {
                    log.info("Sent to partition {} offset {}",
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                })
                .exceptionally(ex -> {
                    log.error("Kafka is not responding. Launching fallback HTTP path for {}. Reason: {}",
                            email, ex.getMessage());

                    sendEmailService.sendViaHttpFallbackDelete(dto);
                    return null;
                });
    }


    protected ProducerRecord createKafkaEvent(EmailDto notification) {
        ProducerRecord<String, EmailDto> producerRecord = new ProducerRecord<>("ui-notification", notification.email(), notification);
        producerRecord.headers().add(new RecordHeader(RECORD_HEADER_KEY, notification.email().getBytes(StandardCharsets.UTF_8)));
        return producerRecord;
    }
}
