package com.microservices.customerService.infrastructure.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {
    private final Long personId;
    private final String customerId;
    private final boolean active;

    // persons
    private final String name;
    private final String gender;
    private final Integer age;
    private final String identification;
    private final String address;
    private final String phone;
}
