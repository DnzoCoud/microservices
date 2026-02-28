package com.microservices.accountService.infrastructure.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountResponse {
    private final Long id;
    private final String accountNumber;
    private final String accountType;
    private final BigDecimal initialBalance;
    private final BigDecimal balance;
    private final boolean active;
    private final String customerId;
}
