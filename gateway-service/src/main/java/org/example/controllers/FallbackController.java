package org.example.controllers;

import org.example.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/user-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDto fallbackUserService(ServerWebExchange exchange) {
        return new ErrorDto(
                "User service is temporarily unavailable. Please try again later.",
                exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR));
    }

    @RequestMapping("/notification-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDto fallbackNotificationService(ServerWebExchange exchange) {
        return new ErrorDto(
                "Notification service is temporarily unavailable. Please try again later.",
                exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR));
    }
}
