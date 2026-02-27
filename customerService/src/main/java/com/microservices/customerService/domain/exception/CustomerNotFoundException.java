package com.microservices.customerService.domain.exception;

import com.microservices.customerService.domain.model.CustomerId;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(CustomerId id) {
        super("Customer not found: " + (id == null ? "null" : id.getValue()));
    }
}
