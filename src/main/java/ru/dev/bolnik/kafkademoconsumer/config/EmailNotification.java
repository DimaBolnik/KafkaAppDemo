package ru.dev.bolnik.kafkademoconsumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EmailNotification {


    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
