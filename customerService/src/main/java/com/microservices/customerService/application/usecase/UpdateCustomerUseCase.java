package com.microservices.customerService.application.usecase;

import com.microservices.customerService.application.dto.UpdateCustomerCommand;
import com.microservices.customerService.application.port.PasswordHasher;
import com.microservices.customerService.domain.exception.CustomerNotFoundException;
import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateCustomerUseCase {
    private final CustomerRepositoryPort repository;
    private final PasswordHasher passwordHasher;
    private final CustomerEventPublisherPort publisher;

    public UpdateCustomerUseCase(
            CustomerRepositoryPort repository,
            PasswordHasher passwordHasher,
            CustomerEventPublisherPort publisher
    ) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.publisher = publisher;
    }

    @Transactional
    public Customer execute(UpdateCustomerCommand cmd) {
        CustomerId customerId = CustomerId.of(cmd.customerId());

        Customer current = repository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String name = cmd.name() != null ? cmd.name() : current.getName();
        String gender = cmd.gender() != null ? cmd.gender() : current.getGender();
        Integer age = cmd.age() != null ? cmd.age() : current.getAge();
        String identification = cmd.identification() != null ? cmd.identification() : current.getIdentification();
        String address = cmd.address() != null ? cmd.address() : current.getAddress();
        String phone = cmd.phone() != null ? cmd.phone() : current.getPhone();

        Customer updated = current.updatePersona(name, gender, age, identification, address, phone);

        if (cmd.active() != null) {
            updated = updated.changeStatus(cmd.active());
        }

        if (cmd.rawPassword() != null && !cmd.rawPassword().isBlank()) {
            String newHash = passwordHasher.hash(cmd.rawPassword());
            updated = updated.changePasswordHash(newHash);
        }

        Customer saved = repository.save(updated);
        publisher.publishUpdated(saved);

        return saved;
    }
}
