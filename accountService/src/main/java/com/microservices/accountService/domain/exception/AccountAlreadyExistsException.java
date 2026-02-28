package com.microservices.accountService.domain.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String accountNumber) {
        super("Account already exists: " + accountNumber);
    }
}
