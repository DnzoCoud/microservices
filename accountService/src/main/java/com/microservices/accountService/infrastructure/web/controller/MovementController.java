package com.microservices.accountService.infrastructure.web.controller;

import com.microservices.accountService.application.dto.CreateMovementCommand;
import com.microservices.accountService.application.usecase.CreateMovementUseCase;
import com.microservices.accountService.infrastructure.web.advice.ApiResponse;
import com.microservices.accountService.infrastructure.web.dto.CreateMovementRequest;
import com.microservices.accountService.infrastructure.web.dto.CreateMovementWithNumber;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/movimientos")
public class MovementController {
    private final CreateMovementUseCase createMovementUseCase;

    public MovementController(CreateMovementUseCase createMovementUseCase) {
        this.createMovementUseCase = createMovementUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createMovement(@RequestBody CreateMovementWithNumber body, HttpServletRequest req) {
        var cmd = new CreateMovementCommand(
            body.getAccountNumber(),
            body.getValue(),
            body.getDate() != null ? body.getDate() : OffsetDateTime.now()
        );

        var saved = createMovementUseCase.execute(cmd);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(saved, req.getRequestURI()));
    }
}
