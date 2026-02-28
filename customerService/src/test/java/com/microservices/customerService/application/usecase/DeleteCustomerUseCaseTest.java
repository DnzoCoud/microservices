package com.microservices.customerService.application.usecase;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCustomerUseCaseTest {
    @Mock
    CustomerRepositoryPort repository;
    @Mock
    CustomerEventPublisherPort publisher;

    @InjectMocks
    DeleteCustomerUseCase useCase;

    @Test
    void shouldDeleteAndPublishEvent() {
        Customer existing = Customer.builder()
                .personId(PersonId.of(10L))
                .customerId(CustomerId.of("CUST-10"))
                .passwordHash("hash")
                .active(true)
                .name("Dani")
                .build();

        when(repository.findByCustomerId(CustomerId.of("CUST-10"))).thenReturn(Optional.of(existing));

        useCase.execute("CUST-10");

        verify(repository).deleteByCustomerId(CustomerId.of("CUST-10"));
        verify(publisher).publishDeleted(CustomerId.of("CUST-10"));
    }

    @Test
    void shouldThrowNotFoundWhenDeletingMissingCustomer() {
        when(repository.findByCustomerId(CustomerId.of("CUST-X"))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> useCase.execute("CUST-X"));

        verify(repository, never()).deleteByCustomerId(any());
        verify(publisher, never()).publishDeleted(any());
    }
}
