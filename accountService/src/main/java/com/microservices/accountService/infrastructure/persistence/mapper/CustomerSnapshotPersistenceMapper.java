package com.microservices.accountService.infrastructure.persistence.mapper;

import com.microservices.accountService.domain.model.CustomerSnapshot;
import com.microservices.accountService.infrastructure.persistence.entity.CustomerSnapshotEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class CustomerSnapshotPersistenceMapper {
    public CustomerSnapshot toDomain(CustomerSnapshotEntity e) {
        if (e == null) return null;
        Instant updated = e.getUpdatedAt() != null ? e.getUpdatedAt().toInstant() : Instant.now();

        return CustomerSnapshot.builder()
                .customerId(e.getCustomerId())
                .name(e.getName())
                .active(e.isStatus())
                .updatedAt(updated)
                .build();
    }

    public OffsetDateTime toOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant != null ? instant : Instant.now(), ZoneOffset.UTC);
    }
}
