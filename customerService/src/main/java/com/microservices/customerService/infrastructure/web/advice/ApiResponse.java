package com.microservices.customerService.infrastructure.web.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ApiError error;

    @Builder.Default
    private final Instant timestamp = Instant.now();
    private final String path;

    public static <T> ApiResponse<T> ok(T data, String path) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .path(path)
                .build();
    }

    public static <T> ApiResponse<T> error(ApiError error, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .path(path)
                .build();
    }
}
