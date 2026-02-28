package com.microservices.accountService.infrastructure.persistence.mapper;

import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.model.AccountId;
import com.microservices.accountService.domain.model.AccountNumber;
import com.microservices.accountService.domain.model.AccountType;
import com.microservices.accountService.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountPersistenceMapper {
    public Account toDomain(AccountEntity e) {
        if(e == null) return null;

        return Account.builder()
                .id(AccountId.of(e.getId()))
                .accountNumber(AccountNumber.of(e.getAccountNumber()))
                .accountType(AccountType.valueOf(e.getAccountType()))
                .initialBalance(e.getInitialBalance())
                .balance(e.getBalance())
                .active(e.isStatus())
                .customerId(e.getCustomerId())
                .build();
    }

    public AccountEntity toEntity(Account d) {
        if(d == null) return null;

        return AccountEntity.builder()
                .id(d.getId().getValue())
                .accountNumber(d.getAccountNumber().getValue())
                .accountType(d.getAccountType().name())
                .initialBalance(d.getInitialBalance())
                .balance(d.getBalance())
                .status(d.isActive())
                .customerId(d.getCustomerId())
                .build();
    }
}
