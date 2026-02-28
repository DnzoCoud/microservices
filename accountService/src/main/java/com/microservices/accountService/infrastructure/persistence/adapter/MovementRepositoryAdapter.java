package com.microservices.accountService.infrastructure.persistence.adapter;

import com.microservices.accountService.domain.model.AccountId;
import com.microservices.accountService.domain.model.Movement;
import com.microservices.accountService.domain.port.MovementRepositoryPort;
import com.microservices.accountService.infrastructure.persistence.mapper.MovementPersistenceMapper;
import com.microservices.accountService.infrastructure.persistence.repository.MovementJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class MovementRepositoryAdapter implements MovementRepositoryPort {

    private final MovementJpaRepository jpa;
    private final MovementPersistenceMapper mapper;

    public MovementRepositoryAdapter(MovementJpaRepository jpa, MovementPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public List<Movement> findByAccountIdBetweenDates(AccountId accountId, OffsetDateTime from, OffsetDateTime to) {
        return jpa.findByAccount_IdAndMovementDateBetweenOrderByMovementDateAsc(
                accountId.getValue(), from, to
        ).stream().map(mapper::toDomain).toList();
    }
}
