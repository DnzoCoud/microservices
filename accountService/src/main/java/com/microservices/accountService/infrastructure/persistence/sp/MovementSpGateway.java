package com.microservices.accountService.infrastructure.persistence.sp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface MovementSpGateway {
    SpResult createMovement(
            Long accountId,
            String movementType,
            BigDecimal amount,
            OffsetDateTime date
    );

    record SpResult(Long movementId, BigDecimal newBalance) {}
}
