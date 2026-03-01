package com.microservices.customerService.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonIdTest {
    @Test
    void shouldCreatePersonId() {
        PersonId id = PersonId.of(10L);
        assertEquals(10L, id.getValue());
    }

    @Test
    void shouldThrowWhenNullOrNonPositive() {
        assertThrows(IllegalArgumentException.class, () -> PersonId.of(null));
        assertThrows(IllegalArgumentException.class, () -> PersonId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> PersonId.of(-1L));
    }
}
