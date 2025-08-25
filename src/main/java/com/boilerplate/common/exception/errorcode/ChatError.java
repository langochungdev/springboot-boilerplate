package com.boilerplate.common.exception.errorcode;
import com.boilerplate.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatError implements BaseErrorCode {
    IS_NOT_MEMBER(403, "IS_NOT_MEMBER", "Không phải thành viên nhóm");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
