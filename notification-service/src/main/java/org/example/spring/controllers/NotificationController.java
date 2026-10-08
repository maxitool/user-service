package org.example.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.kafka.dto.EmailDto;
import org.example.kafka.dto.response.MetaApiResponse;
import org.example.spring.dto.ApiRootDto;
import org.example.spring.hateoas.assemblers.RepresentationAssembler;
import org.example.spring.properties.AppServerProperties;
import org.example.spring.services.NotificationService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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
@Tag(
        name = "Notification",
        description = "Operations for users notification"
)
public class NotificationController {
    private final NotificationService notificationService;
    private final RepresentationAssembler representationAssembler;
    private final AppServerProperties appServerProperties;

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
    public MetaApiResponse<EntityModel<ApiRootDto>> getApi() {
        return addMetaData(representationAssembler.toModel(new ApiRootDto()));
    }

    @Operation(
            summary = "Send user created message to email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User created message was sent"
    )
    @PostMapping("/sendUserCreatedToEmail")
    @ResponseStatus(HttpStatus.OK)
    public MetaApiResponse<Void> sendUserCreatedToEmail(
            @Valid
            @RequestBody
            EmailDto emailDto) {
        notificationService.sendUserCreatedToEmail(emailDto);
        return addMetaData(null);
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
    public MetaApiResponse<Void> sendUserDeletedToEmail(
            @Valid
            @RequestBody
            EmailDto emailDto) {
        notificationService.sendUserDeletedToEmail(emailDto);
        return addMetaData(null);
    }

    private <T> MetaApiResponse<T> addMetaData(T dto) {
        return new MetaApiResponse<>(
                appServerProperties.hostPortMessage(),
                dto
        );
    }
}
