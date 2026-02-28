package com.microservices.accountService.application.usecase;

import com.microservices.accountService.domain.exception.CustomerInactiveException;
import com.microservices.accountService.domain.exception.CustomerSnapshotNotFoundException;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateAccountUseCase {
    private final CustomerSnapshotRepositoryPort snapshotRepo;

    public CreateAccountUseCase(CustomerSnapshotRepositoryPort snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    @Transactional
    public void validateCustomerExistsAndActive(String customerId) {
        var snap = snapshotRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerSnapshotNotFoundException(customerId));

        if (!snap.isActive()) {
            throw new CustomerInactiveException(customerId);
        }
    }
}
