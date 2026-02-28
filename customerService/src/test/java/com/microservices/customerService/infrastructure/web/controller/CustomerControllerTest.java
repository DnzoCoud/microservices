package com.microservices.customerService.infrastructure.web.controller;

import com.microservices.customerService.application.usecase.CreateCustomerUseCase;
import com.microservices.customerService.application.usecase.DeleteCustomerUseCase;
import com.microservices.customerService.application.usecase.ListCustomersUseCase;
import com.microservices.customerService.application.usecase.UpdateCustomerUseCase;
import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.model.PersonId;
import com.microservices.customerService.infrastructure.web.dto.CustomerResponse;
import com.microservices.customerService.infrastructure.web.mapper.CustomerWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean ListCustomersUseCase listCustomersUseCase;
    @MockitoBean
    CreateCustomerUseCase createCustomerUseCase;
    @MockitoBean UpdateCustomerUseCase updateCustomerUseCase;
    @MockitoBean
    DeleteCustomerUseCase deleteCustomerUseCase;
    @MockitoBean
    CustomerWebMapper mapper;

    @Test
    void shouldCreateCustomer() throws Exception {
        Customer saved = Customer.builder()
            .personId(PersonId.of(1L))
            .customerId(CustomerId.of("CUST-100"))
            .passwordHash("hash")
            .active(true)
            .name("Dani")
            .build();

        CustomerResponse response = CustomerResponse.builder()
            .personId(1L)
            .customerId("CUST-100")
            .active(true)
            .name("Dani")
            .build();

        when(createCustomerUseCase.execute(any())).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        var payload = Map.of(
            "customerId", "CUST-100",
            "password", "1234",
            "active", true,
            "name", "Dani"
        );

        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.customerId").value("CUST-100"))
            .andExpect(jsonPath("$.data.name").value("Dani"))
            .andExpect(jsonPath("$.path").value("/api/v1/clientes"));
    }

    @Test
    void shouldListCustomers() throws Exception {
        Customer c1 = Customer.builder()
            .personId(PersonId.of(1L))
            .customerId(CustomerId.of("CUST-1"))
            .passwordHash("hash")
            .active(true)
            .name("A")
            .build();

        Customer c2 = Customer.builder()
            .personId(PersonId.of(2L))
            .customerId(CustomerId.of("CUST-2"))
            .passwordHash("hash")
            .active(true)
            .name("B")
            .build();

        when(listCustomersUseCase.execute()).thenReturn(List.of(c1, c2));

        when(mapper.toResponse(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            return CustomerResponse.builder()
                .personId(c.getPersonId().getValue())
                .customerId(c.getCustomerId().getValue())
                .active(c.isActive())
                .name(c.getName())
                .build();
        });

        mockMvc.perform(get("/api/v1/clientes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].customerId").value("CUST-1"))
            .andExpect(jsonPath("$.data[1].customerId").value("CUST-2"))
            .andExpect(jsonPath("$.path").value("/api/v1/clientes"));

        verify(listCustomersUseCase).execute();
    }
}
