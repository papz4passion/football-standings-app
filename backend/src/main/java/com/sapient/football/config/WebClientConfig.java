package com.sapient.football.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient used to call external API.
 * Implements Singleton pattern for WebClient bean.
 */
@Configuration
public class WebClientConfig {
    
    @Value("${api.football.base-url}")
    private String baseUrl;
    
    @Value("${api.football.timeout:10000}")
    private int timeout;
    
    /**
     * Creates and configures WebClient bean (Singleton).
     * 
     * @return configured WebClient instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
    }
}
