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
public class Customer {
    @EqualsAndHashCode.Include
    private final CustomerId customerId;

    private final String passwordHash;
    private final boolean active;

    private final String name;
    private final String gender;
    private final Integer age;
    private final String identification;
    private final String address;
    private final String phone;

    /**
     * Basic invariants for the domain model.
     * Keep it simple: validations for REST belong in DTOs.
     */
    private Customer(
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
        if (customerId == null) throw new IllegalArgumentException("customerId must not be null");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("passwordHash must not be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");

        this.customerId = customerId;
        this.passwordHash = passwordHash;
        this.active = active;

        this.name = name;
        this.gender = gender;
        this.age = age;
        this.identification = identification;
        this.address = address;
        this.phone = phone;
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