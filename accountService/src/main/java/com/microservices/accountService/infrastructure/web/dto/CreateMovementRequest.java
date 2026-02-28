package com.microservices.accountService.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CreateMovementRequest {
    @NotNull
    private BigDecimal value;
    private OffsetDateTime date;
}
