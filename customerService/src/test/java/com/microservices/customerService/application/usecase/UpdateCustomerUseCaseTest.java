package com.microservices.customerService.application.usecase;

import com.microservices.customerService.application.dto.UpdateCustomerCommand;
import com.microservices.customerService.application.port.PasswordHasher;
import com.microservices.customerService.domain.exception.CustomerNotFoundException;
import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.model.PersonId;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCustomerUseCaseTest {
    @Mock
    CustomerRepositoryPort repo;
    @Mock
    CustomerEventPublisherPort publisher;
    @Mock
    PasswordHasher passwordHasher;

    @InjectMocks UpdateCustomerUseCase useCase;

    @Test
    void shouldUpdateCustomerAndPublishEvent() {
        Customer current = (Customer) baseCustomer("CUST-1")
                .name("Old Name")
                .address("Old Address")
                .build();

        when(repo.findByCustomerId(CustomerId.of("CUST-1"))).thenReturn(Optional.of(current));
        when(passwordHasher.hash("new password")).thenReturn("hashed");

        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        UpdateCustomerCommand cmd = new UpdateCustomerCommand(
            "CUST-1",
            "New Name",
            null,
            null,
            null,
            "New Address",
            null,
            "newPass",
            false
        );

        Customer updated = useCase.execute(cmd);

        assertEquals("CUST-1", updated.getCustomerId().getValue());
        assertEquals("New Name", updated.getName());
        assertEquals("New Address", updated.getAddress());
        assertFalse(updated.isActive());
        assertEquals("hashedPass", updated.getPasswordHash());

        verify(passwordHasher).hash("newPass");
        verify(repo).save(any(Customer.class));
        verify(publisher).publishUpdated(any(Customer.class));
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() {
        when(repo.findByCustomerId(CustomerId.of("CUST-404"))).thenReturn(Optional.empty());

        UpdateCustomerCommand cmd = new UpdateCustomerCommand(
            "CUST-404",
            "X", null, null, null, null, null,
            null,
            null
        );

        assertThrows(CustomerNotFoundException.class, () -> useCase.execute(cmd));

        verify(repo, never()).save(any());
        verify(publisher, never()).publishUpdated(any());
        verify(passwordHasher, never()).hash(any());
    }

    private static Customer.CustomerBuilder baseCustomer(String customerId) {
        return Customer.builder()
                .personId(PersonId.of(1L))
                .customerId(CustomerId.of(customerId))
                .passwordHash("oldHash")
                .active(true)
                .name("Base Name")
                .gender(null)
                .age(null)
                .identification(null)
                .address(null)
                .phone(null);
    }
}
