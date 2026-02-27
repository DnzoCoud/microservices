package com.microservices.accountService.domain.exception;

public class CustomerSnapshotNotFoundException extends RuntimeException {
    public CustomerSnapshotNotFoundException(String customerId) {
        super("Customer not found in snapshot: " + customerId);
    }
}
