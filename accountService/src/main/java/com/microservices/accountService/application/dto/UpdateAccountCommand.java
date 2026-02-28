package com.microservices.accountService.application.dto;

public record UpdateAccountCommand(
        String accountType,
        boolean active
) {
}
