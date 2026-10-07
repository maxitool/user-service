package org.example.spring.eureka;

import org.example.kafka.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(EurekaUri.NOTIFICATION_SERVICE)
public interface NotificationServiceClient {

    @PostMapping(EurekaUri.NOTIFICATION_SEND_CREATED_USER)
    ResponseEntity<Void> sendUserCreatedToEmail(EmailDto emailDto);

    @PostMapping(EurekaUri.NOTIFICATION_SEND_DELETED_USER)
    ResponseEntity<Void> sendUserDeletedToEmail(EmailDto emailDto);
}
