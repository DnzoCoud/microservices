package com.microservices.customerService.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerIdTest {

    @Test
    void shouldCreateCustomerId_andTrim() {
        CustomerId id = CustomerId.of("  CUST-001  ");
        assertEquals("CUST-001", id.getValue());
    }

    @Test
    void shouldThrowWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> CustomerId.of(" "));
        assertThrows(IllegalArgumentException.class, () -> CustomerId.of(null));
    }
}
