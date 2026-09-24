package org.example.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.SendToEmailCreateDeleteDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class SendEmailService {

    private final RestClient restClient;

    public SendEmailService() {
        this.restClient = RestClient.create();
    }

    private static final String FALL_BACK_URL_DELETED = "http://localhost:8081/api/notification/sendUserDeletedToEmail";
    private static final String FALL_BACK_URL_CREATE = "http://localhost:8081/api/notification/sendUserCreatedToEmail";

    public void sendViaHttpFallbackDelete(SendToEmailCreateDeleteDto dto) {

        String userEmail = dto.email();

        try {

            restClient.get()
                    .uri(FALL_BACK_URL_DELETED, uriBuilder -> uriBuilder
                            .queryParam("email", userEmail)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
            log.info("Fallback HTTP request for user {} successfully sent to the endpoint", userEmail);
        } catch (Exception httpEx) {
            log.error("Error sending fallback HTTP request for user {}: {}", userEmail, httpEx.getMessage(), httpEx);
        }
    }

    public void sendViaHttpFallbackCreate(SendToEmailCreateDeleteDto dto) {

        String userEmail = dto.email();

        try {

            restClient.get()
                    .uri(FALL_BACK_URL_CREATE, uriBuilder -> uriBuilder
                            .queryParam("email", userEmail)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
            log.info("Fallback HTTP request for user {} successfully sent to the endpoint", userEmail);
        } catch (Exception httpEx) {
            log.error("Error sending fallback HTTP request for user {}: {}", userEmail, httpEx.getMessage(), httpEx);
        }
    }
}
