package com.example.identity.exception;

import com.example.identity.constant.ErrorCode;
import com.example.identity.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<String>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null));
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<String>> handlingAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = exception.getBindingResult().getAllErrors()
                    .stream()
                    .findFirst()
                    .map(error -> error.unwrap(ConstraintViolation.class))
                    .orElse(null);
            if (constraintViolation != null) {
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                log.info("Constraint attributes: {}", attributes);
            }
            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
        }

        String message = (attributes != null)
                ? mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage();

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse<>(errorCode.getCode(), message, null));
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
