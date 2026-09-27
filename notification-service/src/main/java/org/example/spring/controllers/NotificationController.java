package org.example.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kafka.EmailDto;
import org.example.spring.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Notification",
        description = "Operations for users notification"
)
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(
            summary = "Send user created message to email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User created message was sent"
    )
    @PostMapping("/sendUserCreatedToEmail")
    @ResponseStatus(HttpStatus.OK)
    public void sendUserCreatedToEmail(@Valid @RequestBody EmailDto emailDto) {
        notificationService.sendUserCreatedToEmail(emailDto);
    }

    @Operation(
            summary = "Send user deleted message to email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User deleted message was sent"
    )
    @PostMapping("/sendUserDeletedToEmail")
    @ResponseStatus(HttpStatus.OK)
    public void sendUserDeletedToEmail(@Valid @RequestBody EmailDto emailDto) {
        notificationService.sendUserDeletedToEmail(emailDto);
    }
}
