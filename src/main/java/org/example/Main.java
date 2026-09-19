package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        SpringApplication.run(Main.class, args);
    }
}
