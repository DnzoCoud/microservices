package com.microservices.accountService.infrastructure.web.controller;

import com.microservices.accountService.application.usecase.GetAccountStatementReportUseCase;
import com.microservices.accountService.infrastructure.web.advice.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/reportes", "/api/v1/reportes"})
public class ReportController {
    private final GetAccountStatementReportUseCase getAccountStatementReportUseCase;

    public ReportController(GetAccountStatementReportUseCase getAccountStatementReportUseCase) {
        this.getAccountStatementReportUseCase = getAccountStatementReportUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> report(
            @RequestParam("fecha") String fecha,
            @RequestParam(value = "clienteId", required = false) String clienteId,
            @RequestParam(value = "customerId", required = false) String customerId,
            HttpServletRequest req
    ) {
        String id = (clienteId != null && !clienteId.isBlank()) ? clienteId : customerId;
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("clienteId (or customerId) is required");
        }

        var report = getAccountStatementReportUseCase.execute(id, fecha);
        return ResponseEntity.ok(ApiResponse.ok(report, req.getRequestURI()));
    }
}
