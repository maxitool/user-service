package org.example.spring.events.listeners;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.Operation;
import org.example.spring.events.UserCreatedDeletedEvent;
import org.example.spring.kafka.EmailDtoProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
public class UserCreatedDeletedEventListener {
    private final Map<Operation, Consumer<String>> methods;

    public UserCreatedDeletedEventListener(EmailDtoProducer kafkaProducer) {
        if (kafkaProducer == null) {
            log.error("kafkaProducer is null");
            methods = Map.of();
            return;
        }
        methods = Map.of(
                Operation.CREATE, kafkaProducer::sendToEmailUserCreated,
                Operation.DELETE, kafkaProducer::sendToEmailUserDeleted
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedDeletedEvent(UserCreatedDeletedEvent event) {
        log.debug("Database {} transaction successfully committed. Sending dto to Kafka for email: {}",
                event.operation(), event.email());
        if (!methods.containsKey(event.operation())) {
            String message = "Unknown operation: '" + event.operation()
                    + "'. Allowed values: " + Arrays.toString(methods.keySet().toArray());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        methods.get(event.operation()).accept(event.email());
    }
}
