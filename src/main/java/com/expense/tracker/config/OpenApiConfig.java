package com.expense.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration for OpenAPI / Swagger UI documentation
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Expense Tracker API")
                        .version("1.0.0")
                        .description("REST API for tracking personal expenses")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")));
    }
}
