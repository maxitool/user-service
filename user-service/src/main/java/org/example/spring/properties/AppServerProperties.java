package org.example.spring.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class AppServerProperties {

    @Value("${spring.application.name:UNKNOWN-SERVICE}")
    private String serverHost;

    @Value("${HOST_PORT:0}")
    private Integer serverPort;

    public String hostPortMessage() {
        return String.format(
                "Called function from %s:%d rest controller",
                serverHost == null ? "UNKNOWN" : serverHost.toUpperCase(),
                serverPort == null ? 0 : serverPort
        );
    }
}
