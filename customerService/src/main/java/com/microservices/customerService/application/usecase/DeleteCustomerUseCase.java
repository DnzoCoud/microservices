package com.microservices.customerService.application.usecase;

import com.microservices.customerService.domain.exception.CustomerNotFoundException;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerUseCase {
    private final CustomerRepositoryPort repository;
    private final CustomerEventPublisherPort publisher;

    public DeleteCustomerUseCase(CustomerRepositoryPort repository, CustomerEventPublisherPort publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public void execute(String customerId) {
        CustomerId validationId = CustomerId.of(customerId);

        var current = repository.findByCustomerId(validationId)
                .orElseThrow(() -> new CustomerNotFoundException(validationId));

        current.changeStatus(false);
        repository.save(current);
        publisher.publishDeleted(validationId);
    }
}
