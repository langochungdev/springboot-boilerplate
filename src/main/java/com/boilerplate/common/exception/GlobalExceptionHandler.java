package com.boilerplate.common.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, WebRequest request) {
        BaseErrorCode ec = ex.getErrorCode();
        ApiError error = new ApiError(
                ec.getStatus(),
                ec.getCode(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(ec.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, WebRequest request) {
        ApiError error = new ApiError(
                500,
                "INTERNAL_ERROR",
                "Có lỗi xảy ra, vui lòng thử lại sau",
                request.getDescription(false)
        );
        return ResponseEntity.status(500).body(error);
    }
}
