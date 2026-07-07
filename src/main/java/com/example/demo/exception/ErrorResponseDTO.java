package com.example.demo.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponseDTO(
    Instant timestamp,
    int status,
    String error,
    String message,
    List<String> details
) {
    public ErrorResponseDTO(int status, String error, String message) {
        this(Instant.now(), status, error, message, null);
    }

    public ErrorResponseDTO(int status, String error, String message, List<String> details) {
        this(Instant.now(), status, error, message, details);
    }
}