package com.microservices.accountService.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AccountId {
    private final Long value;

    private AccountId(Long value) { this.value = value; }

    public static AccountId of(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("accountId must be positive");
        return new AccountId(value);
    }
}
