package com.microservices.accountService.infrastructure.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class MovementResponse {
    private final Long movementId;
    private final String accountNumber;
    private final String movementType;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final OffsetDateTime date;
}
