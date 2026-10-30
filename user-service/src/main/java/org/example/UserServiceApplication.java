package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                description = "REST API for managing users",
                version = "1.0"
        )
)
@EnableHypermediaSupport(type = {
        EnableHypermediaSupport.HypermediaType.HAL,
        EnableHypermediaSupport.HypermediaType.HAL_FORMS
})
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.example.spring.feign")
public class UserServiceApplication {
    static void main(String[] args) {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
