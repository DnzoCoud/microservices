package com.microservices.accountService.domain.exception;

public class CustomerInactiveException extends RuntimeException {
    public CustomerInactiveException(String customerId) {
        super("Customer is inactive: " + customerId);
    }
}
