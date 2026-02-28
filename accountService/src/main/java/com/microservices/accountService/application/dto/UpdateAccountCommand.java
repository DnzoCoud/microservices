package com.microservices.accountService.application.dto;

public record UpdateAccountCommand(
        String accountNumber,
        String accountType,
        boolean active
) {
}
