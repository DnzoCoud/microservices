package com.microservices.accountService.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class CustomerSnapshot {
    private final String customerId;
    private final String name;
    private final boolean active;
    private final Instant updatedAt;

    private CustomerSnapshot(String customerId, String name, boolean active, Instant updatedAt) {
        if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId must not be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        this.customerId = customerId;
        this.name = name;
        this.active = active;
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }
}
