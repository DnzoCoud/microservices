package com.microservices.customerService.application.dto;

public record UpdateCustomerCommand(
    String customerId,

    String name,
    String gender,
    Integer age,
    String identification,
    String address,
    String phone,

    String rawPassword,
    Boolean active
) {
}
