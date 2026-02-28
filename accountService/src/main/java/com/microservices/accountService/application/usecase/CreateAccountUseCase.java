package com.microservices.accountService.application.usecase;

import com.microservices.accountService.application.dto.CreateAccountCommand;
import com.microservices.accountService.domain.exception.AccountAlreadyExistsException;
import com.microservices.accountService.domain.exception.CustomerInactiveException;
import com.microservices.accountService.domain.exception.CustomerSnapshotNotFoundException;
import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.model.AccountNumber;
import com.microservices.accountService.domain.model.AccountType;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateAccountUseCase {
    private final CustomerSnapshotRepositoryPort snapshotRepo;
    private final AccountRepositoryPort repo;


    public CreateAccountUseCase(CustomerSnapshotRepositoryPort snapshotRepo, AccountRepositoryPort repo) {
        this.snapshotRepo = snapshotRepo;
        this.repo = repo;
    }

    @Transactional
    public void validateCustomerExistsAndActive(String customerId) {
        var snap = snapshotRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerSnapshotNotFoundException(customerId));

        if (!snap.isActive()) {
            throw new CustomerInactiveException(customerId);
        }
    }

    @Transactional
    public Account execute(CreateAccountCommand cmd) {
        AccountNumber accountNumber = AccountNumber.of(cmd.accountNumber());

        if (repo.existsByAccountNumber(accountNumber.getValue())) {
            throw new AccountAlreadyExistsException(accountNumber.getValue());
        }

        validateCustomerExistsAndActive(accountNumber.getValue());

        BigDecimal initial = cmd.initialBalance() != null ? cmd.initialBalance() : BigDecimal.ZERO;

        Account toCreate = Account.builder()
                .id(null)
                .accountNumber(accountNumber)
                .accountType(AccountType.valueOf(cmd.accountType()))
                .initialBalance(initial)
                .balance(initial)
                .active(cmd.active())
                .customerId(cmd.customerId())
                .build();

        return repo.save(toCreate);
    }
}
