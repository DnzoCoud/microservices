package com.microservices.customerService.infrastructure.messaging.dto;

public record CustomerEventData(
    String customerId,
    String name,
    boolean status
) {}
