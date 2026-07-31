package com.expense.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to customize the OpenAPI generation for Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Expense Tracker API")
                        .version("1.0.0")
                        .description("Production-quality REST API documentation for the Smart Expense Tracker built with Spring Boot.")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")));
    }
}
