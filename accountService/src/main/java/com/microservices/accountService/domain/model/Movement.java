package com.microservices.accountService.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class Movement {
    private final Long id;
    private final AccountId accountId;
    private final Instant date;
    private final AccountType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;

    private Movement(Long id, AccountId accountId, Instant date, AccountType type, BigDecimal amount, BigDecimal balanceAfter) {
        if (accountId == null) throw new IllegalArgumentException("accountId must not be null");
        if (type == null) throw new IllegalArgumentException("type must not be null");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");
        if (balanceAfter == null) throw new IllegalArgumentException("balanceAfter must not be null");

        this.id = id;
        this.accountId = accountId;
        this.date = date != null ? date : Instant.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }
}
