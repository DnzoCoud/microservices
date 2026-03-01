package com.microservices.customerService.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDomainTest {
    @Test
    void shouldBuildCustomer() {
        Customer c = baseCustomer().build();

        assertEquals("CUST-1", c.getCustomerId().getValue());
        assertEquals("Dani", c.getName());
        assertTrue(c.isActive());
        assertNotNull(c.getPersonId());
    }

    @Test
    void updatePersona_shouldReturnNewInstance_andKeepOriginal() {
        Customer original = baseCustomer()
                .address("Old Address")
                .phone("111")
                .build();

        Customer updated = original.updatePersona(
                "New Name",
                "M",
                20,
                "CC-123",
                "New Address",
                "222"
        );

        // original unchanged
        assertEquals("Dani", original.getName());
        assertEquals("Old Address", original.getAddress());
        assertEquals("111", original.getPhone());

        // updated changed
        assertEquals("New Name", updated.getName());
        assertEquals("New Address", updated.getAddress());
        assertEquals("222", updated.getPhone());

        // identity unchanged
        assertEquals(original.getCustomerId(), updated.getCustomerId());
        assertEquals(original.getPersonId(), updated.getPersonId());
    }

    @Test
    void changeStatus_shouldReturnNewInstance() {
        Customer original = baseCustomer().active(true).build();
        Customer disabled = original.changeStatus(false);

        assertTrue(original.isActive());
        assertFalse(disabled.isActive());
        assertEquals(original.getCustomerId(), disabled.getCustomerId());
    }

    @Test
    void changePasswordHash_shouldReturnNewInstance() {
        Customer original = baseCustomer().passwordHash("oldHash").build();
        Customer changed = original.changePasswordHash("newHash");

        assertEquals("oldHash", original.getPasswordHash());
        assertEquals("newHash", changed.getPasswordHash());
    }

    @Test
    void toString_shouldNotLeakPasswordHash() {
        Customer c = baseCustomer().passwordHash("superSecretHash").build();
        String s = c.toString();
        assertFalse(s.contains("superSecretHash"), "toString must not leak passwordHash");
    }

    private static Customer.CustomerBuilder<?, ?> baseCustomer() {
        return Customer.builder()
                .personId(PersonId.of(1L))
                .customerId(CustomerId.of("CUST-1"))
                .passwordHash("hash")
                .active(true)
                .name("Dani")
                .gender(null)
                .age(null)
                .identification(null)
                .address(null)
                .phone(null);
    }
}
