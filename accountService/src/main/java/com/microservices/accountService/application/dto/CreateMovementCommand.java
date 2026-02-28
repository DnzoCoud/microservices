package com.microservices.accountService.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateMovementCommand(
    String accountNumber,
    BigDecimal value,
    OffsetDateTime date
) {}
