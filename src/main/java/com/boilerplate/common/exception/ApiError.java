package com.boilerplate.common.exception;

public record ApiError(
        int status,
        String code,
        String message,
        String path
) {}
