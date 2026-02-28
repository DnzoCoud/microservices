package com.microservices.accountService.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record AccountStatementReport(
        Customer customer,
        String from,
        String to,
        List<AccountItem> cuentas
) {
    public record Customer(String customerId, String name, boolean active) {}

    public record AccountItem(
            String accountNumber,
            String accountType,
            BigDecimal initialBalance,
            BigDecimal balance,
            boolean active,
            List<MovementItem> movements
    ) {}

    public record MovementItem(
            Instant date,
            String type,
            BigDecimal amount,
            BigDecimal balanceAfter
    ) {}
}
