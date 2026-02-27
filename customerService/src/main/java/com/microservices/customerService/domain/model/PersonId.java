package com.microservices.customerService.domain.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PersonId {
    private final Long value;

    private PersonId(Long value) {
        this.value = value;
    }

    public static PersonId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("personId must be a positive number");
        }
        return new PersonId(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
