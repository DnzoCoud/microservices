package com.microservices.accountService.domain.model;

import lombok.Getter;

@Getter
public enum CustomerEventTypes {
    CUSTOMER_CREATED("customer.created"),
    CUSTOMER_UPDATED("customer.updated"),
    CUSTOMER_DELETED("customer.deleted"),;

    private final String type;

    CustomerEventTypes(String type) {
        this.type = type;
    }
}
