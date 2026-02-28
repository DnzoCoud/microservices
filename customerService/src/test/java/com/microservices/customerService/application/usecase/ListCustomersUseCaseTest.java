package com.microservices.customerService.application.usecase;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.model.PersonId;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCustomersUseCaseTest {
    @Mock
    CustomerRepositoryPort repository;

    @InjectMocks
    ListCustomersUseCase useCase;

    @Test
    void shouldReturnAllCustomers() {
        var c1 = Customer.builder()
                .personId(PersonId.of(1L))
                .customerId(CustomerId.of("CUST-1"))
                .passwordHash("hash")
                .active(true)
                .name("A")
                .build();

        var c2 = Customer.builder()
                .personId(PersonId.of(2L))
                .customerId(CustomerId.of("CUST-2"))
                .passwordHash("hash")
                .active(true)
                .name("B")
                .build();

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        var result = useCase.execute();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}
