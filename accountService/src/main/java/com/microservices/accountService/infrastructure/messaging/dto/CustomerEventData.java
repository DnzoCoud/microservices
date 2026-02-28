package com.microservices.accountService.infrastructure.messaging.dto;

public record CustomerEventData(
        String customerId,
        String name,
        boolean status
) {}
