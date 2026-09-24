package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                description = "REST API for managing users",
                version = "1.0"
        )
)
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
