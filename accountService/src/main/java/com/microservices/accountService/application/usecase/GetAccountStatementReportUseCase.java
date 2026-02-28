package com.microservices.accountService.application.usecase;

import com.microservices.accountService.application.dto.AccountStatementReport;
import com.microservices.accountService.domain.exception.CustomerSnapshotNotFoundException;
import com.microservices.accountService.domain.port.AccountRepositoryPort;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import com.microservices.accountService.domain.port.MovementRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class GetAccountStatementReportUseCase {
    private final CustomerSnapshotRepositoryPort snapshots;
    private final AccountRepositoryPort accounts;
    private final MovementRepositoryPort movements;

    public GetAccountStatementReportUseCase(
            CustomerSnapshotRepositoryPort snapshots,
            AccountRepositoryPort accounts,
            MovementRepositoryPort movements
    ) {
        this.snapshots = snapshots;
        this.accounts = accounts;
        this.movements = movements;
    }

    @Transactional(readOnly = true)
    public AccountStatementReport execute(String customerId, String fechaRange) {
        var range = parseFecha(fechaRange);

        var customer = snapshots.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerSnapshotNotFoundException(customerId));

        var customerDto = new AccountStatementReport.Customer(customer.getCustomerId(), customer.getName(), customer.isActive());
        var accs = accounts.findByCustomerId(customer.getCustomerId());

        List<AccountStatementReport.AccountItem> items = accs.stream().map(a -> {
            var mvs = movements.findByAccountIdBetweenDates(a.getId(), range.from, range.to).stream()
                    .map(m -> new AccountStatementReport.MovementItem(
                            m.getDate(),
                            m.getType().getAccountType(),
                            m.getAmount(),
                            m.getBalanceAfter()
                    ))
                    .toList();

            return new AccountStatementReport.AccountItem(
                    a.getAccountNumber().getValue(),
                    a.getAccountType().getAccountType(),
                    a.getInitialBalance(),
                    a.getBalance(),
                    a.isActive(),
                    mvs
            );
        }).toList();

        return new AccountStatementReport(customerDto, range.fromDate.toString(), range.toDate.toString(), items);
    }

    private static DateRange parseFecha(String fechaRange) {
        if (fechaRange == null || fechaRange.isBlank()) {
            throw new IllegalArgumentException("fecha is required. Format: YYYY-MM-DD,YYYY-MM-DD");
        }

        String[] parts = fechaRange.contains("|")
                ? fechaRange.split("\\|")
                : fechaRange.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid fecha format. Use: YYYY-MM-DD,YYYY-MM-DD");
        }

        LocalDate fromDate = LocalDate.parse(parts[0].trim());
        LocalDate toDate = LocalDate.parse(parts[1].trim());

        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("Invalid fecha range: end date is before start date");
        }

        OffsetDateTime from = fromDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = toDate.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).minusNanos(1);

        return new DateRange(fromDate, toDate, from, to);
    }

    private record DateRange(LocalDate fromDate, LocalDate toDate, OffsetDateTime from, OffsetDateTime to) {}
}
