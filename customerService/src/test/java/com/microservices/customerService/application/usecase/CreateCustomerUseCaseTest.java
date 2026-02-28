package com.microservices.customerService.application.usecase;

import com.microservices.customerService.application.dto.CreateCustomerCommand;
import com.microservices.customerService.application.port.PasswordHasher;
import com.microservices.customerService.domain.exception.CustomerAlreadyExistsException;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCustomerUseCaseTest {
    @Mock
    CustomerRepositoryPort repo;
    @Mock
    CustomerEventPublisherPort publisher;
    @Mock
    PasswordHasher hasher;

    @InjectMocks CreateCustomerUseCase useCase;

    @Test
    void shouldCreateCustomerAndPublishEvent() {
        when(repo.existsByCustomerId(any())).thenReturn(false);
        when(hasher.hash("1234")).thenReturn("hash");
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        var command = new CreateCustomerCommand("CUST-1", "1234", true, "Dani", null, null, null, null, null);
        var saved = useCase.execute(command);

        verify(repo).save(any());
        verify(publisher).publishCreated(any());
        assertEquals("CUST-1", saved.getCustomerId().getValue());
    }

    @Test
    void shouldFailIfCustomerAlreadyExists() {
        when(repo.existsByCustomerId(any())).thenReturn(true);
        var command = new CreateCustomerCommand("CUST-1", "1234", true, "Dani", null, null, null, null, null);

        assertThrows(CustomerAlreadyExistsException.class, () -> useCase.execute(command));
        verify(repo, never()).save(any());
        verify(publisher, never()).publishCreated(any());
    }

}
