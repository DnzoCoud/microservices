package com.microservices.accountService.infrastructure.persistence.repository;

import com.microservices.accountService.infrastructure.persistence.entity.CustomerSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface CustomerSnapshotJpaRepository extends JpaRepository<CustomerSnapshotEntity, Long> {
    Optional<CustomerSnapshotEntity> findByCustomerIdAndStatusTrue(String customerId);

    void deleteByCustomerId(String customerId);

    @Modifying
    @Query(value = """
    INSERT INTO account.customer_snapshot(customer_id, name, status, updated_at)
    VALUES (:customerId, :name, :status, :updatedAt)
    ON CONFLICT (customer_id)
    DO UPDATE SET
      name = EXCLUDED.name,
      status = EXCLUDED.status,
      updated_at = EXCLUDED.updated_at
    """, nativeQuery = true)
    void upsert(
            @Param("customerId") String customerId,
            @Param("name") String name,
            @Param("status") boolean status,
            @Param("updatedAt") OffsetDateTime updatedAt
    );
}
