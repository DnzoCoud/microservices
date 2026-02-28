package com.microservices.accountService.domain.port;

import com.microservices.accountService.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    Account save(Account account);
    List<Account> findByCustomerId(String customerId);
    List<Account> findAll();
    void deleteByAccountNumber(String accountNumber);
}
