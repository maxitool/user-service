package org.example.spring.dto.notification_service;

public record NotificationDto(String userEmail,
                              String operationType,
                              String message) {
}
