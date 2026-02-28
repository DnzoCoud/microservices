package com.microservices.accountService.infrastructure.persistence.adapter;

import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import com.microservices.accountService.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.microservices.accountService.infrastructure.persistence.repository.AccountJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository repo;
    private final AccountPersistenceMapper mapper;

    public AccountRepositoryAdapter(AccountJpaRepository repo, AccountPersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return repo.findByAccountNumber(accountNumber).map(mapper::toDomain);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repo.existsByAccountNumber(accountNumber);
    }

    @Override
    public Account save(Account account) {
        return mapper.toDomain(repo.save(mapper.toEntity(account)));
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        return repo.findByCustomerId(customerId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Account> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        repo.deleteByAccountNumber(accountNumber);
    }
}
