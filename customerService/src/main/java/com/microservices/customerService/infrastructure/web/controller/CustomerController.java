package com.microservices.customerService.infrastructure.web.controller;

import com.microservices.customerService.application.dto.CreateCustomerCommand;
import com.microservices.customerService.application.usecase.CreateCustomerUseCase;
import com.microservices.customerService.application.usecase.ListCustomersUseCase;
import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.infrastructure.web.advice.ApiResponse;
import com.microservices.customerService.infrastructure.web.dto.CreateCustomerRequest;
import com.microservices.customerService.infrastructure.web.mapper.CustomerWebMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Customers", description = "Customer endpoints")
@RestController
@RequestMapping("/api/v1/clientes")
public class CustomerController {
    private final ListCustomersUseCase listCustomers;
    private final CreateCustomerUseCase createCustomer;
    private final CustomerWebMapper mapper;

    public CustomerController(ListCustomersUseCase listCustomers, CustomerWebMapper mapper, CreateCustomerUseCase createCustomer) {
        this.listCustomers = listCustomers;
        this.mapper = mapper;
        this.createCustomer = createCustomer;
    }

    @GetMapping
    public ApiResponse<List<?>> list(HttpServletRequest req) {
        var customers = listCustomers.execute().stream().map(mapper::toResponse).toList();
        return ApiResponse.ok(customers, req.getRequestURI());
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody CreateCustomerRequest body,  HttpServletRequest req) {
        var cmd = new CreateCustomerCommand(
                body.getCustomerId(),
                body.getPassword(),
                body.getActive() != null ? body.getActive() : true,
                body.getName(),
                body.getGender(),
                body.getAge(),
                body.getIdentification(),
                body.getAddress(),
                body.getPhone()
        );

        var saved = createCustomer.execute(cmd);
        var response = mapper.toResponse(saved);
        return ApiResponse.ok(response, req.getRequestURI());
    }
}
