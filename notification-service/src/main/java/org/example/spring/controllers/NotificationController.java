package org.example.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kafka.EmailDto;
import org.example.spring.dto.ApiRootDto;
import org.example.spring.hateoas.assemblers.RepresentationAssembler;
import org.example.spring.services.NotificationService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/api/v1/email",
        produces = {
                "application/prs.hal-forms+json",
                "application/hal+json",
                "application/json"
        }
)
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Notification",
        description = "Operations for users notification"
)
public class NotificationController {
    private final NotificationService notificationService;
    private final RepresentationAssembler representationAssembler;

    @Operation(
            summary = "Get api",
            description = "Returns notification api"
    )
    @ApiResponse(
            responseCode = "200",
            description = "api",
            content = @Content(
                    schema = @Schema(implementation = ApiRootDto.class)
            )
    )
    @GetMapping("/getApi")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<ApiRootDto> getApi() {
        return representationAssembler.toModel(new ApiRootDto());
    }

    @Operation(
            summary = "Send user created message to email"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User created message was sent"
    )
    @PostMapping("/sendUserCreatedToEmail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> sendUserCreatedToEmail(
            @Valid
            @RequestBody
            EmailDto emailDto) {
        notificationService.sendUserCreatedToEmail(emailDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Send user deleted message to email"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User deleted message was sent"
    )
    @PostMapping("/sendUserDeletedToEmail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> sendUserDeletedToEmail(
            @Valid
            @RequestBody
            EmailDto emailDto) {
        notificationService.sendUserDeletedToEmail(emailDto);
        return ResponseEntity.noContent().build();
    }
}
