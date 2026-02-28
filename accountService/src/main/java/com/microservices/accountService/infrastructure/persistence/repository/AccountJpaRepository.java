package com.microservices.accountService.infrastructure.persistence.repository;

import com.microservices.accountService.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<AccountEntity> findByCustomerId(String customerId);
    void deleteByAccountNumber(String accountNumber);
}
