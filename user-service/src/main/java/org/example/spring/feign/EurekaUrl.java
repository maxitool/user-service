package org.example.spring.feign;

public class EurekaUrl {
    private EurekaUrl() {
    }

    public static final String NOTIFICATION_SERVICE = "NOTIFICATION-SERVICE";
    private static final String NOTIFICATION_COMMON = "/api/v1/email";
    public static final String NOTIFICATION_SEND_CREATED_USER = NOTIFICATION_COMMON + "/sendUserCreatedToEmail";
    public static final String NOTIFICATION_SEND_DELETED_USER = NOTIFICATION_COMMON + "/sendUserDeletedToEmail";
}
