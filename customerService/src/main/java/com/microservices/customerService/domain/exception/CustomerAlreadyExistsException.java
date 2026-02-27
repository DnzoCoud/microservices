package com.microservices.customerService.domain.exception;

import com.microservices.customerService.domain.model.CustomerId;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(CustomerId id) {
        super("Customer already exists: " + (id == null ? "null" : id.getValue()));
    }
}
