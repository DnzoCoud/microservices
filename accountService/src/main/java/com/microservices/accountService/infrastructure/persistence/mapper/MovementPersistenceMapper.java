package com.microservices.accountService.infrastructure.persistence.mapper;

import com.microservices.accountService.domain.model.AccountId;
import com.microservices.accountService.domain.model.AccountType;
import com.microservices.accountService.domain.model.Movement;
import com.microservices.accountService.domain.model.MovementType;
import com.microservices.accountService.infrastructure.persistence.entity.MovementEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementPersistenceMapper {
    public Movement toDomain(MovementEntity e) {
        return Movement.builder()
                .id(e.getId())
                .accountId(AccountId.of(e.getAccount().getId()))
                .date(e.getMovementDate().toInstant())
                .type(AccountType.from(e.getMovementType()))
                .amount(e.getAmount())
                .balanceAfter(e.getBalanceAfter())
                .build();
    }
}
