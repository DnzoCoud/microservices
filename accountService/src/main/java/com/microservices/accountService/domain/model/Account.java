package com.microservices.accountService.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @EqualsAndHashCode.Include
    private final AccountId id;
    private final AccountNumber accountNumber;
    private final AccountType accountType;
    private final BigDecimal initialBalance;
    private final BigDecimal balance;
    private final boolean active;

    private final String customerId;

    private Account(
            AccountId id,
            AccountNumber accountNumber,
            AccountType accountType,
            BigDecimal initialBalance,
            BigDecimal balance,
            boolean active,
            String customerId
    ) {
        if (accountNumber == null) throw new IllegalArgumentException("accountNumber must not be null");
        if (accountType == null) throw new IllegalArgumentException("accountType must not be null");
        if (initialBalance == null) throw new IllegalArgumentException("initialBalance must not be null");
        if (balance == null) throw new IllegalArgumentException("balance must not be null");
        if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId must not be blank");

        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.balance = balance;
        this.active = active;
        this.customerId = customerId;
    }

    public Account changeStatus(boolean active) {
        return this.toBuilder().active(active).build();
    }
}
