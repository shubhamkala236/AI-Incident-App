package org.shub.userservice.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

public record ExceptionResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    public static ExceptionResponse of(HttpStatus status, String message, String path) {
        return new ExceptionResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }

    public static ExceptionResponse of(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        return new ExceptionResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, fieldErrors);
    }
}
