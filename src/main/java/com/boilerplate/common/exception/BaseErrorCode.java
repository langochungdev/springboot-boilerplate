package com.boilerplate.common.exception;

public interface BaseErrorCode {
    int getStatus();
    String getCode();
    String getMessage();
    String formatMessage(Object... args);
}
