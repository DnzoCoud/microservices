package com.microservices.accountService.domain.port;

import com.microservices.accountService.domain.model.CustomerSnapshot;

import java.util.Optional;

public interface CustomerSnapshotRepositoryPort {
    Optional<CustomerSnapshot> findByCustomerId(String customerId);
    CustomerSnapshot upsert(CustomerSnapshot snapshot);
    void deleteByCustomerId(String customerId);
}
