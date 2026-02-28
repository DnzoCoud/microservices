package com.microservices.accountService.application.dto;

import java.math.BigDecimal;

public record CreateAccountCommand(
    String accountNumber,
    String accountType,
    BigDecimal initialBalance,
    boolean active,
    String customerId
) {}
