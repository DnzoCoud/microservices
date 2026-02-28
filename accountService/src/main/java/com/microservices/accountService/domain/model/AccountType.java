package com.microservices.accountService.domain.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AccountType {
    SAVINGS("Ahorros"), CHECKING("Corriente");

    private final String accountType;
    AccountType(String accountType) {
        this.accountType = accountType;
    }

    public static AccountType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("accountType must not be blank");
        }

        String v = value.trim();
        for (AccountType t : values()) {
            if (t.name().equalsIgnoreCase(v)) return t;
        }

        for (AccountType t : values()) {
            if (t.accountType.equalsIgnoreCase(v)) return t;
        }

        throw new IllegalArgumentException("Invalid accountType: " + value +
                ". Allowed: " + Arrays.toString(values()) + " or labels: Ahorros/Corriente");
    }
}
