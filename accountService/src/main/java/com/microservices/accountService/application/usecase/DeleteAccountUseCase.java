package com.microservices.accountService.application.usecase;

import com.microservices.accountService.domain.exception.AccountNotFoundException;
import com.microservices.accountService.domain.model.AccountNumber;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAccountUseCase {
    private final AccountRepositoryPort repo;

    public DeleteAccountUseCase(AccountRepositoryPort repo) {
        this.repo = repo;
    }

    @Transactional
    public void execute(String accountNumber) {
        AccountNumber accountNumberObj =  AccountNumber.of(accountNumber);

        if (!repo.existsByAccountNumber(accountNumberObj.getValue())) {
            throw new AccountNotFoundException(accountNumber);
        }

        repo.deleteByAccountNumber(accountNumberObj.getValue());
    }
}
