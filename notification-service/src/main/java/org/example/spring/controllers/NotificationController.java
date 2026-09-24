package org.example.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.spring.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/sendUserCreatedToEmail")
    @ResponseStatus(HttpStatus.OK)
    public void sendUserCreatedToEmail(@RequestParam String email) {
        notificationService.sendUserCreatedToEmail(email);
    }

    @Operation(
            summary = "Send user deleted message to email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User deleted message was sent"
    )
    @GetMapping("/sendUserDeletedToEmail")
    @ResponseStatus(HttpStatus.OK)
    public void sendUserDeletedToEmail(@RequestParam String email) {
        notificationService.sendUserDeletedToEmail(email);
    }
}
