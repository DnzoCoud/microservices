package com.microservices.customerService.application.port;

/**
 * Abstraction for hashing raw passwords.
 */
public interface PasswordHasher {
    String hash(String password);
}
