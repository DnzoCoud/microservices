package com.microservices.accountService.domain.exception;

public class CustomerSnapshotNotFoundException extends RuntimeException {
  public CustomerSnapshotNotFoundException(String message) {
    super(message);
  }
}
