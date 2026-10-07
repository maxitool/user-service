package org.example.spring.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.example.kafka.CommunicationData;
import org.example.kafka.EmailDto;
import org.example.spring.eureka.NotificationServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProducerIntegrationTest {

    @Mock
    private KafkaTemplate<String, EmailDto> kafkaTemplate;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private EmailDtoProducer emailDtoProducer;

    @Captor
    private ArgumentCaptor<ProducerRecord<String, EmailDto>> recordCaptor;

    @Test
    void when_sendToEmailUserDeleted_then_success() {

        String testEmail = "test@example.com";
        EmailDto testDto = new EmailDto(testEmail);

        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(100L);

        ProducerRecord<String, EmailDto> dummyRecord = new ProducerRecord<>("email-dto", testEmail, testDto);
        SendResult<String, EmailDto> sendResult = new SendResult<>(dummyRecord, recordMetadata);

        CompletableFuture<SendResult<String, EmailDto>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        emailDtoProducer.sendToEmailUserDeleted(testEmail);

        verify(kafkaTemplate).send(recordCaptor.capture());

        ProducerRecord<String, EmailDto> sentRecord = recordCaptor.getValue();

        assertThat(sentRecord).isNotNull();
        assertThat(sentRecord.value()).isNotNull();
        assertThat(sentRecord.value().email()).isEqualTo(testEmail);

        Header operationHeader = sentRecord.headers().lastHeader(CommunicationData.RECORD_HEADER_OPERATION_KEY);
        assertThat(operationHeader).isNotNull();

        String operationValue = new String(operationHeader.value(), StandardCharsets.UTF_8);
        assertThat(operationValue).isEqualTo("DELETE");
    }

    @Test
    void when_sendToEmailUserCreated_then_success() {
        String testEmail = "create@example.com";
        EmailDto testDto = new EmailDto(testEmail);

        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(200L);

        ProducerRecord<String, EmailDto> dummyRecord = new ProducerRecord<>(CommunicationData.TOPIC, testEmail, testDto);
        SendResult<String, EmailDto> sendResult = new SendResult<>(dummyRecord, recordMetadata);

        CompletableFuture<SendResult<String, EmailDto>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);
        emailDtoProducer.sendToEmailUserCreated(testEmail);

        verify(kafkaTemplate).send(recordCaptor.capture());
        ProducerRecord<String, EmailDto> sentRecord = recordCaptor.getValue();

        assertThat(sentRecord).isNotNull();
        assertThat(sentRecord.value().email()).isEqualTo(testEmail);

        Header operationHeader = sentRecord.headers().lastHeader(CommunicationData.RECORD_HEADER_OPERATION_KEY);
        assertThat(operationHeader).isNotNull();
        assertThat(new String(operationHeader.value(), StandardCharsets.UTF_8)).isEqualTo("CREATE");
    }

    @Test
    void when_send_then_error() {
        String testEmail = "fallback@example.com";

        CompletableFuture<SendResult<String, EmailDto>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka connection timeout"));

        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(failedFuture);

        emailDtoProducer.sendToEmailUserDeleted(testEmail);

        verify(notificationServiceClient, times(1)).sendUserDeletedToEmail(any(EmailDto.class));
    }

}
