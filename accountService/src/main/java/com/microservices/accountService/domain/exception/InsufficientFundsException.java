package com.microservices.accountService.domain.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient balance");
    }
}
