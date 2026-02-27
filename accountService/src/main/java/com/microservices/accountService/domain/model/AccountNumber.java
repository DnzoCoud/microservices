package com.microservices.accountService.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AccountNumber {
    private final String value;

    private AccountNumber(String value) { this.value = value; }

    public static AccountNumber of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("accountNumber must not be blank");
        return new AccountNumber(value.trim());
    }

    @Override public String toString() { return value; }
}
