package com.microservices.customerService.application.usecase;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCustomersUseCase {
    private final CustomerRepositoryPort repository;

    public ListCustomersUseCase(CustomerRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Returns all customers.
     */
    public List<Customer> execute() {
        return repository.findAll();
    }
}
