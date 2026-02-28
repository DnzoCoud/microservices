package com.microservices.accountService.domain.port;

import com.microservices.accountService.domain.model.Account;

import java.util.Optional;

public interface AccountRepositoryPort {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    Account save(Account account);
}
