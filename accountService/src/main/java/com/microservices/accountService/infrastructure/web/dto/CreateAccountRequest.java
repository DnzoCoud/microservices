package com.microservices.accountService.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {
    @NotBlank private String accountNumber;
    @NotBlank private String accountType;
    @NotNull  private BigDecimal initialBalance;
    @NotNull  private Boolean active;
    @NotBlank private String customerId;
}
