package com.campusnaksha.identity.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(
            EmailAlreadyExistsException exception,
            HttpServletRequest request
    ) {

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "EMAIL_ALREADY_EXISTS",
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
public ResponseEntity<ApiError> handleInvalidCredentials(
        InvalidCredentialsException exception,
        HttpServletRequest request
) {

    ApiError error = new ApiError(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "INVALID_CREDENTIALS",
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
    );

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(error);
}

@ExceptionHandler(AccountDisabledException.class)
public ResponseEntity<ApiError> handleAccountDisabled(
        AccountDisabledException exception,
        HttpServletRequest request
) {

    ApiError error = new ApiError(
            Instant.now(),
            HttpStatus.FORBIDDEN.value(),
            "ACCOUNT_DISABLED",
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
    );

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error);
}
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ApiError> handleUserNotFound(
        UserNotFoundException exception,
        HttpServletRequest request
) {

    ApiError error = new ApiError(
            Instant.now(),
            HttpStatus.NOT_FOUND.value(),
            "USER_NOT_FOUND",
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}
@ExceptionHandler(InvalidRefreshTokenException.class)
public ResponseEntity<ApiError> handleInvalidRefreshToken(
        InvalidRefreshTokenException exception,
        HttpServletRequest request
) {

    ApiError error = new ApiError(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "INVALID_REFRESH_TOKEN",
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
    );

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(error);
}

private static final Logger log =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<ApiError> handleUnreadableRequest(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
) {

    return ResponseEntity
            .badRequest()
            .body(
                    new ApiError(
                            Instant.now(),
                            400,
                            "INVALID_REQUEST",
                            "Request body is malformed or contains an invalid value.",
                            request.getRequestURI(),
                            Map.of()
                    )
            );
}
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ApiError> handleDataIntegrity(
        DataIntegrityViolationException exception,
        HttpServletRequest request
) {

    log.warn(
            "Database integrity violation on {}",
            request.getRequestURI(),
            exception
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                    new ApiError(
                            Instant.now(),
                            409,
                            "DATA_INTEGRITY_VIOLATION",
                            "The request conflicts with existing data or database constraints.",
                            request.getRequestURI(),
                            Map.of()
                    )
            );
}
@ExceptionHandler(Exception.class)
public ResponseEntity<ApiError> handleUnexpected(
        Exception exception,
        HttpServletRequest request
) {

    log.error(
            "Unhandled exception on {}",
            request.getRequestURI(),
            exception
    );

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                    new ApiError(
                            Instant.now(),
                            500,
                            "INTERNAL_SERVER_ERROR",
                            "An unexpected server error occurred.",
                            request.getRequestURI(),
                            Map.of()
                    )
            );
}
}