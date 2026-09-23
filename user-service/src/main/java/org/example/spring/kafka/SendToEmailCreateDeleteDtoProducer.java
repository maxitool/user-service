package org.example.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.Operation;
import org.example.SendToEmailCreateDeleteDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendToEmailCreateDeleteDtoProducer {
    private final KafkaTemplate<String, SendToEmailCreateDeleteDto> kafkaTemplate;

    public SendToEmailCreateDeleteDtoProducer(KafkaTemplate<String, SendToEmailCreateDeleteDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToEmailUserCreated(Long id, String email) {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.CREATE, email);
        kafkaTemplate.send("send-to-email-create-delete-dto", id + "id" + dto.hashCode(), dto)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent to partition {} offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void sendToEmailUserDeleted(Long id, String email) {
        SendToEmailCreateDeleteDto dto = new SendToEmailCreateDeleteDto(Operation.DELETE, email);
        kafkaTemplate.send("send-to-email-create-delete-dto", id + "id" + dto.hashCode(), dto)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent to partition {} offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
