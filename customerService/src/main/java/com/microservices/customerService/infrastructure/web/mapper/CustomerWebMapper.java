package com.microservices.customerService.infrastructure.web.mapper;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.infrastructure.web.dto.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerWebMapper {
    public CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .personId(c.getPersonId() != null ? c.getPersonId().getValue() : null)
                .customerId(c.getCustomerId().getValue())
                .active(c.isActive())
                .name(c.getName())
                .gender(c.getGender())
                .age(c.getAge())
                .identification(c.getIdentification())
                .address(c.getAddress())
                .phone(c.getPhone())
                .build();
    }
}
