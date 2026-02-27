package com.microservices.customerService.infrastructure.web.swagger;

import com.microservices.customerService.infrastructure.web.advice.ApiError;
import com.microservices.customerService.infrastructure.web.advice.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Service API")
                        .version("v1")
                        .description("Microservice responsible for Customer and Person management."));
    }

}
