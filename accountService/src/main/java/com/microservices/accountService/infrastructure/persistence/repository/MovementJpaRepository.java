package com.microservices.accountService.infrastructure.persistence.repository;

import com.microservices.accountService.infrastructure.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {
    List<MovementEntity> findByAccount_IdAndMovementDateBetweenOrderByMovementDateAsc(Long accountId, OffsetDateTime from, OffsetDateTime to);
}
