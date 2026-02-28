package com.microservices.accountService.infrastructure.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest {
    private String accountNumber;
    private String accountType;
    private boolean active;
}
