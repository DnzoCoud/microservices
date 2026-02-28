package com.microservices.accountService.infrastructure.persistence.adapter;

import com.microservices.accountService.domain.model.CustomerSnapshot;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import com.microservices.accountService.infrastructure.persistence.mapper.CustomerSnapshotPersistenceMapper;
import com.microservices.accountService.infrastructure.persistence.repository.CustomerSnapshotJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class CustomerSnapshotRepositoryAdapter implements CustomerSnapshotRepositoryPort {
    private final CustomerSnapshotJpaRepository jpa;
    private final CustomerSnapshotPersistenceMapper mapper;

    public CustomerSnapshotRepositoryAdapter(CustomerSnapshotJpaRepository jpa, CustomerSnapshotPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<CustomerSnapshot> findByCustomerId(String customerId) {
        return jpa.findByCustomerIdAndStatusTrue(customerId).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public CustomerSnapshot upsert(CustomerSnapshot snapshot) {
        OffsetDateTime updatedAt = mapper.toOffsetDateTime(snapshot.getUpdatedAt());
        jpa.upsert(snapshot.getCustomerId(), snapshot.getName(), snapshot.isActive(), updatedAt);
        return findByCustomerId(snapshot.getCustomerId()).orElse(snapshot);
    }

    @Override
    @Transactional
    public void deleteByCustomerId(String customerId) {
        jpa.deleteByCustomerId(customerId);
    }
}
