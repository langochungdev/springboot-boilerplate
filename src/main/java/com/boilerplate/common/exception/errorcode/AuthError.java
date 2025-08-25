package com.boilerplate.common.exception.errorcode;

import com.boilerplate.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements BaseErrorCode {
    MISSING_TOKEN(401, "MISSING_TOKEN", "Cookie không có token"),
    INVALID_TOKEN(401, "INVALID_TOKEN", "Token sai hoặc đã hết hạn"),
    BLACKLISTED_TOKEN(401, "BLACKLISTED_TOKEN", "Token đã bị vô hiệu hóa (blacklist)");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
