package com.microservices.accountService.infrastructure.web.advice;

import com.microservices.accountService.domain.exception.CustomerInactiveException;
import com.microservices.accountService.domain.exception.CustomerSnapshotNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerSnapshotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(CustomerSnapshotNotFoundException ex, HttpServletRequest req) {
        var err = ApiError.builder()
                .code("CUSTOMER_NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(err, req.getRequestURI()));
    }

    @ExceptionHandler(CustomerInactiveException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(CustomerInactiveException ex, HttpServletRequest req) {
        var err = ApiError.builder()
                .code("CUSTOMER_INACTIVE")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(err, req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(e -> {
                    if (e instanceof FieldError fe) {
                        return fe.getField() + ": " + fe.getDefaultMessage();
                    }
                    return e.getDefaultMessage();
                })
                .toList();

        var err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message("Request validation failed")
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(err, req.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        var err = ApiError.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(err, req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex, HttpServletRequest req) {
        var err = ApiError.builder()
                .code("INTERNAL_ERROR")
                .message("Unexpected error")
                .details(List.of(ex.getClass().getSimpleName() + ": " + safeMsg(ex.getMessage())))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(err, req.getRequestURI()));
    }

    private String safeMsg(String msg) {
        return msg == null ? "" : msg.replaceAll("[\\r\\n\\t]", " ").trim();
    }
}
