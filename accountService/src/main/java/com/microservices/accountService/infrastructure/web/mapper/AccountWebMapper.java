package com.microservices.accountService.infrastructure.web.mapper;

import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.infrastructure.web.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountWebMapper {
    public AccountResponse toResponse(Account a) {
        return AccountResponse.builder()
            .id(a.getId().getValue() != null ? a.getId().getValue() : null)
            .accountNumber(a.getAccountNumber().getValue())
            .accountType(a.getAccountType().getAccountType())
            .initialBalance(a.getInitialBalance())
            .balance(a.getBalance())
            .active(a.isActive())
            .customerId(a.getCustomerId())
            .build();
    }
}
