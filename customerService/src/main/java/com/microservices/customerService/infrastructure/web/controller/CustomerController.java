package com.microservices.customerService.infrastructure.web.controller;

import com.microservices.customerService.application.dto.CreateCustomerCommand;
import com.microservices.customerService.application.dto.UpdateCustomerCommand;
import com.microservices.customerService.application.usecase.CreateCustomerUseCase;
import com.microservices.customerService.application.usecase.DeleteCustomerUseCase;
import com.microservices.customerService.application.usecase.ListCustomersUseCase;
import com.microservices.customerService.application.usecase.UpdateCustomerUseCase;
import com.microservices.customerService.infrastructure.web.advice.ApiResponse;
import com.microservices.customerService.infrastructure.web.dto.CreateCustomerRequest;
import com.microservices.customerService.infrastructure.web.dto.UpdateCustomerRequest;
import com.microservices.customerService.infrastructure.web.mapper.CustomerWebMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customers", description = "Customer endpoints")
@RestController
@RequestMapping("/api/v1/clientes")
public class CustomerController {
    private final ListCustomersUseCase listCustomers;
    private final CreateCustomerUseCase createCustomer;
    private final CustomerWebMapper mapper;
    private final UpdateCustomerUseCase updateCustomer;
    private final DeleteCustomerUseCase deleteCustomer;

    public CustomerController(
            ListCustomersUseCase listCustomers,
            CustomerWebMapper mapper,
            CreateCustomerUseCase createCustomer,
            UpdateCustomerUseCase updateCustomer,
            DeleteCustomerUseCase deleteCustomer
    ) {
        this.listCustomers = listCustomers;
        this.mapper = mapper;
        this.createCustomer = createCustomer;
        this.updateCustomer = updateCustomer;
        this.deleteCustomer = deleteCustomer;
    }

    @GetMapping
    public ApiResponse<List<?>> list(HttpServletRequest req) {
        var customers = listCustomers.execute().stream().map(mapper::toResponse).toList();
        return ApiResponse.ok(customers, req.getRequestURI());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>>  create(@RequestBody CreateCustomerRequest body,  HttpServletRequest req) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, req.getRequestURI()));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequest body,
            HttpServletRequest req
    ) {
        var cmd = new UpdateCustomerCommand(
                customerId,
                body.getName(),
                body.getGender(),
                body.getAge(),
                body.getIdentification(),
                body.getAddress(),
                body.getPhone(),
                body.getPassword(),
                body.getActive()
        );

        var saved = this.updateCustomer.execute(cmd);
        var response = mapper.toResponse(saved);

        return ResponseEntity.ok(ApiResponse.ok(response, req.getRequestURI()));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> delete(
            @PathVariable String customerId,
            HttpServletRequest req
    ) {
        if (customerId == null) throw new IllegalArgumentException("customerId is null");

        deleteCustomer.execute(customerId);

        return ResponseEntity.noContent().build();
    }

}
