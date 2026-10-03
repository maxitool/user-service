package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@OpenAPIDefinition(
        info = @Info(
                title = "Notification Service API",
                description = "REST API for send messages to email",
                version = "1.0"
        )
)
@EnableHypermediaSupport(type = {
        EnableHypermediaSupport.HypermediaType.HAL,
        EnableHypermediaSupport.HypermediaType.HAL_FORMS
})
@SpringBootApplication
public class NotificationServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
