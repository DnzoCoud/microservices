package com.microservices.customerService.infrastructure.web.dto.command;

/**
 * Application command (not a REST DTO).
 */
public record CreateCustomerCommand (
    String customerId,
    String rawPassword,
    boolean active,
    String name,
    String gender,
    Integer age,
    String identification,
    String address,
    String phone
) {}
