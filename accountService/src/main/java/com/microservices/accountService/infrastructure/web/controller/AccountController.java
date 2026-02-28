package com.microservices.accountService.infrastructure.web.controller;

import com.microservices.accountService.application.dto.CreateAccountCommand;
import com.microservices.accountService.application.dto.CreateMovementCommand;
import com.microservices.accountService.application.dto.UpdateAccountCommand;
import com.microservices.accountService.application.usecase.*;
import com.microservices.accountService.infrastructure.web.advice.ApiResponse;
import com.microservices.accountService.infrastructure.web.dto.*;
import com.microservices.accountService.infrastructure.web.mapper.AccountWebMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
public class AccountController {
    private final ListAccountUseCase listAccountUseCase;
    private final AccountWebMapper mapper;
    private final CreateAccountUseCase createAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final CreateMovementUseCase createMovementUseCase;

    public AccountController(
        ListAccountUseCase listAccountUseCase,
        AccountWebMapper mapper,
        CreateAccountUseCase createAccountUseCase,
        UpdateAccountUseCase updateAccountUseCase,
        DeleteAccountUseCase deleteAccountUseCase,
        GetAccountUseCase getAccountUseCase,
        CreateMovementUseCase createMovementUseCase
    ) {
        this.listAccountUseCase = listAccountUseCase;
        this.mapper = mapper;
        this.createAccountUseCase = createAccountUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.createMovementUseCase = createMovementUseCase;
    }

    @GetMapping("/{accountNumber}")
    public ApiResponse<AccountResponse> get(@PathVariable String accountNumber, HttpServletRequest req) {
        var account = mapper.toResponse(getAccountUseCase.execute(accountNumber));
        return ApiResponse.ok(account, req.getRequestURI());
    }

    @GetMapping
    public ApiResponse<List<?>> list(@RequestParam(required = false) String customerId, HttpServletRequest req) {
        var accounts = listAccountUseCase.execute(customerId).stream().map(mapper::toResponse).toList();
        return ApiResponse.ok(accounts, req.getRequestURI());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody CreateAccountRequest body, HttpServletRequest req) {
        var cmd = new CreateAccountCommand(
            body.getAccountNumber(),
                body.getAccountType(),
                body.getInitialBalance(),
                body.getActive(),
                body.getCustomerId()
        );

        var saved = createAccountUseCase.execute(cmd);
        var response = mapper.toResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, req.getRequestURI()));
    }

    @PostMapping("/{accountNumber}/movimiento")
    public ResponseEntity<ApiResponse<?>> createMovement(
            @PathVariable String accountNumber,
            @RequestBody CreateMovementRequest body,
            HttpServletRequest req
    ) {
        var cmd = new CreateMovementCommand(
                accountNumber,
                body.getValue(),
                body.getDate() != null ? body.getDate() : OffsetDateTime.now()
        );

        var saved = createMovementUseCase.execute(cmd);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(saved, req.getRequestURI()));
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable String accountNumber,
            @RequestBody UpdateAccountRequest body,
            HttpServletRequest req
    ) {
        var cmd = new UpdateAccountCommand(body.getAccountType(), body.isActive());
        var saved = updateAccountUseCase.execute(accountNumber, cmd);
        return ResponseEntity.ok(ApiResponse.ok(mapper.toResponse(saved), req.getRequestURI()));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> delete(@PathVariable String accountNumber) {
        deleteAccountUseCase.execute(accountNumber);
        return ResponseEntity.noContent().build();
    }
}
