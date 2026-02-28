package com.microservices.accountService.application.usecase;

import com.microservices.accountService.domain.exception.AccountNotFoundException;
import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.model.AccountNumber;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetAccountUseCase {
    private final AccountRepositoryPort repo;

    public GetAccountUseCase(AccountRepositoryPort repo) {
        this.repo = repo;
    }

    public Account execute(String accountNumber) {
        return repo.findByAccountNumber(AccountNumber.of(accountNumber).getValue())
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
