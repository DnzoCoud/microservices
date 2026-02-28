package com.microservices.accountService.infrastructure.web.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI accountOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Account Service API")
                        .version("v1")
                        .description("Microservice responsible for Accounts and Movements management."));
    }
}
