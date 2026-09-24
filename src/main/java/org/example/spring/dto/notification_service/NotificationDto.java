package org.example.spring.dto.notification_service;

import org.example.spring.enums.TypeOperation;

public record NotificationDto(String userEmail,
                              TypeOperation operationType,
                              String message) {

    public static NotificationDto of(String userEmail,
                                     TypeOperation operationType,
                                     String message) {
        return new NotificationDto(userEmail, operationType, message);
    }

}
