package org.example.spring.feign;

import org.example.kafka.dto.EmailDto;
import org.example.kafka.dto.response.MetaApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(EurekaUrl.NOTIFICATION_SERVICE)
public interface NotificationServiceClient {

    @PostMapping(EurekaUrl.NOTIFICATION_SEND_CREATED_USER)
    ResponseEntity<MetaApiResponse<Void>> sendUserCreatedToEmail(EmailDto emailDto);

    @PostMapping(EurekaUrl.NOTIFICATION_SEND_DELETED_USER)
    ResponseEntity<MetaApiResponse<Void>> sendUserDeletedToEmail(EmailDto emailDto);
}
