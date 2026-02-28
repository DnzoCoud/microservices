package com.microservices.accountService.application.usecase;

import com.microservices.accountService.application.dto.UpdateAccountCommand;
import com.microservices.accountService.domain.exception.AccountNotFoundException;
import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.model.AccountNumber;
import com.microservices.accountService.domain.model.AccountType;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAccountUseCase {
    private final AccountRepositoryPort repo;

    public UpdateAccountUseCase(AccountRepositoryPort repo) {
        this.repo = repo;
    }

    @Transactional
    public Account execute(UpdateAccountCommand cmd) {
        Account current = repo.findByAccountNumber(AccountNumber.of(cmd.accountNumber()).getValue())
                .orElseThrow(() -> new AccountNotFoundException(cmd.accountNumber()));

        Account updated = current;

        if (cmd.accountType() != null && !cmd.accountType().isBlank()) {
            updated = updated.toBuilder()
                    .accountType(AccountType.valueOf(cmd.accountType()))
                    .build();
        }

        if (cmd.active() != current.isActive()) {
            updated = updated.changeStatus(cmd.active());
        }

        return repo.save(updated);
    }
}
