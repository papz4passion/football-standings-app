package com.sapient.football.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI footballStandingsOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");
        
        Contact contact = new Contact();
        contact.setName("Sapient Assessment Team");
        contact.setEmail("support@sapient.com");
        
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
        
        Info info = new Info()
                .title("Football Standings API")
                .version("1.0.0")
                .contact(contact)
                .description("RESTful API for fetching football standings, leagues, teams, and countries data")
                .termsOfService("https://sapient.com/terms")
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
