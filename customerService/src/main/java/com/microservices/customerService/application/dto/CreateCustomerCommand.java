package com.microservices.customerService.application.dto;

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
