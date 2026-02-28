package com.microservices.customerService.application.usecase;

import com.microservices.customerService.application.dto.CreateCustomerCommand;
import com.microservices.customerService.application.port.PasswordHasher;
import com.microservices.customerService.domain.exception.CustomerAlreadyExistsException;
import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCustomerUseCase {
    private final CustomerRepositoryPort repository;
    private final PasswordHasher passwordHasher;
    private final CustomerEventPublisherPort publisher;

    public CreateCustomerUseCase(
            CustomerRepositoryPort repository,
            PasswordHasher passwordHasher,
            CustomerEventPublisherPort publisher
    ) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.publisher = publisher;
    }

    @Transactional
    public Customer execute(CreateCustomerCommand cmd) {
        var customerId = CustomerId.of(cmd.customerId());

        if (repository.existsByCustomerId(customerId)) {
            throw new CustomerAlreadyExistsException(customerId);
        }

        String passwordHash = passwordHasher.hash(cmd.rawPassword());

        Customer toCreate = Customer.builder()
                .personId(null)
                .name(cmd.name())
                .gender(cmd.gender())
                .age(cmd.age())
                .identification(cmd.identification())
                .address(cmd.address())
                .phone(cmd.phone())
                .customerId(customerId)
                .passwordHash(passwordHash)
                .active(cmd.active())
                .build();

        Customer saved = repository.save(toCreate);
        publisher.publishCreated(saved);

        return saved;
    }
}
