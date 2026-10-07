package org.example.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.EmailDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;


// по сути класс больше не нужен circuit breaker сам будет отправлять сообщения
// пока не удаляла
@Slf4j
@Component
public class SendEmailService {

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public SendEmailService() {
        this.restClient = RestClient.create("http://notification-service:8081");
        this.mapper = new ObjectMapper();
    }

    private static final String FALL_BACK_URL_DELETED = "/api/notification/sendUserDeletedToEmail";
    private static final String FALL_BACK_URL_CREATED = "/api/notification/sendUserCreatedToEmail";

    public void sendViaHttpFallbackDelete(EmailDto emailDto) {
        sendViaHttpFallbackWithEmailDto(emailDto, FALL_BACK_URL_DELETED);
    }

    public void sendViaHttpFallbackCreate(EmailDto emailDto) {
        sendViaHttpFallbackWithEmailDto(emailDto, FALL_BACK_URL_CREATED);
    }

    private void sendViaHttpFallbackWithEmailDto(EmailDto emailDto, String url) {
        try {
            restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(emailDto))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Fallback HTTP request for user {} successfully sent to the endpoint", emailDto.email());
        } catch (Exception httpEx) {
            log.error("Error sending fallback HTTP request for user {}: {}", emailDto.email(), httpEx.getMessage(), httpEx);
        }
    }
}
