package com.microservices.customerService.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class CustomerId {
    private final String value;

    private CustomerId(String value) {
        this.value = value;
    }

    public static CustomerId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("customerId must not be null or blank");
        }
        return new CustomerId(value.trim());
    }

    @Override
    public String toString() {
        return value;
    }
}