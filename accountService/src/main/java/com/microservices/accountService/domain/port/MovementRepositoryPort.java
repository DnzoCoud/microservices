package com.microservices.accountService.domain.port;

import com.microservices.accountService.domain.model.AccountId;
import com.microservices.accountService.domain.model.Movement;

import java.time.OffsetDateTime;
import java.util.List;

public interface MovementRepositoryPort {
    List<Movement> findByAccountIdBetweenDates(AccountId accountId, OffsetDateTime from, OffsetDateTime to);
}
