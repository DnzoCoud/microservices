package com.microservices.customerService.domain.port;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findByCustomerId(CustomerId customerId);
    boolean existsByCustomerId(CustomerId customerId);
    List<Customer> findAll();
    void deleteByCustomerId(CustomerId customerId);
}
