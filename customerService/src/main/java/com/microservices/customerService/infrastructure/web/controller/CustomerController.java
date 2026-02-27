package com.microservices.customerService.infrastructure.web.controller;

import com.microservices.customerService.application.usecase.ListCustomersUseCase;
import com.microservices.customerService.infrastructure.web.advice.ApiResponse;
import com.microservices.customerService.infrastructure.web.mapper.CustomerWebMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Customers", description = "Customer endpoints")
@RestController
@RequestMapping("/api/v1/clientes")
public class CustomerController {
    private final ListCustomersUseCase listCustomers;
    private final CustomerWebMapper mapper;

    public CustomerController(ListCustomersUseCase listCustomers, CustomerWebMapper mapper) {
        this.listCustomers = listCustomers;
        this.mapper = mapper;
    }

    @GetMapping
    public ApiResponse<List<?>> list(HttpServletRequest req) {
        var customers = listCustomers.execute().stream().map(mapper::toResponse).toList();
        return ApiResponse.ok(customers, req.getRequestURI());
    }
}
