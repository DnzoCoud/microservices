package com.microservices.accountService.application.usecase;

import com.microservices.accountService.application.dto.CreateMovementCommand;
import com.microservices.accountService.domain.exception.AccountNotFoundException;
import com.microservices.accountService.domain.exception.InsufficientFundsException;
import com.microservices.accountService.domain.model.Movement;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import com.microservices.accountService.infrastructure.persistence.sp.MovementSpGateway;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateMovementUseCase {
    private final AccountRepositoryPort port;
    private final MovementSpGateway sp;

    public CreateMovementUseCase(AccountRepositoryPort port, MovementSpGateway sp) {
        this.port = port;
        this.sp = sp;
    }

    @Transactional
    public MovementSpGateway.SpResult execute(CreateMovementCommand cmd) {
        if (cmd.value() == null || cmd.value().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        var account = port.findByAccountNumber(cmd.accountNumber());
        if (account.isEmpty()) throw new AccountNotFoundException(cmd.accountNumber());

        String movementType = cmd.value().signum() > 0 ? "CREDIT" : "DEBIT";
        BigDecimal amount = cmd.value().abs();

        try {
            return sp.createMovement(account.get().getId().getValue(), movementType, amount, cmd.date());
        } catch (DataAccessException ex) {
            String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
            if (msg != null && msg.contains("Insufficient balance")) {
                throw new InsufficientFundsException();
            }
            throw ex;
        }
    }
}
