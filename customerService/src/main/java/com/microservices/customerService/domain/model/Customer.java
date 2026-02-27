package com.microservices.customerService.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain model representing Persona + Cliente.
 * Framework-agnostic: no Spring/JPA annotations.
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "passwordHash")
public class Customer extends Person{
    @EqualsAndHashCode.Include
    private final CustomerId customerId;
    private final String passwordHash;
    private final boolean active;

    /**
     * Basic invariants for the domain model.
     * Keep it simple: validations for REST belong in DTOs.
     */
    private Customer(
            PersonId personId,
            CustomerId customerId,
            String passwordHash,
            boolean active,
            String name,
            String gender,
            Integer age,
            String identification,
            String address,
            String phone
    ) {
        super(personId, name, gender, age, identification, address, phone);

        if (customerId == null) {
            throw new IllegalArgumentException("customerId must not be null");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must not be blank");
        }

        this.customerId = customerId;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    public Customer changePasswordHash(String newPasswordHash) {
        return this.toBuilder()
                .passwordHash(newPasswordHash)
                .build();
    }

    public Customer changeStatus(boolean active) {
        return this.toBuilder()
                .active(active)
                .build();
    }
}