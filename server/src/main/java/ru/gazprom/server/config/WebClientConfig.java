package ru.gazprom.server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class
WebClientConfig {
    private final String SERVICE_URL;

    public WebClientConfig(@Value("${app.url.service-stock}") String SERVICE_URL) {
        this.SERVICE_URL = SERVICE_URL;
    }

    @Bean
    public WebClient startWebClient() {
        return WebClient.builder()
                .baseUrl(SERVICE_URL)
                .build();
    }
}
