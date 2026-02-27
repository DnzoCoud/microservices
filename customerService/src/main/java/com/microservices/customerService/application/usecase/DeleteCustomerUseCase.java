package com.microservices.customerService.application.usecase;

import com.microservices.customerService.domain.exception.CustomerNotFoundException;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerUseCase {
    private final CustomerRepositoryPort repository;

    public DeleteCustomerUseCase(CustomerRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(String customerId) {
        CustomerId validationId = CustomerId.of(customerId);

        if (!repository.existsByCustomerId(validationId)) {
            throw new CustomerNotFoundException(validationId);
        }

        repository.deleteByCustomerId(validationId);
    }
}
