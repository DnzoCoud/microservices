package com.microservices.accountService.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovementWithNumber extends CreateMovementRequest {
    @NotNull
    private String accountNumber;
}
