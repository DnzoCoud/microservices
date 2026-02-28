package com.microservices.accountService.application.usecase;

import com.microservices.accountService.domain.model.Account;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAccountUseCase {
    private AccountRepositoryPort repo;

    public ListAccountUseCase(AccountRepositoryPort repo) {
        this.repo = repo;
    }

    public List<Account> execute(String customerId) {
        if (customerId != null && !customerId.isBlank()) {
            return repo.findByCustomerId(customerId);
        }
        return repo.findAll();
    }
}
