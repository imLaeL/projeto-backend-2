package com.aula.pos.appinscricao.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String message,
        List<FieldErrorResponse> errors
) {
    public record FieldErrorResponse(String field, String message) {}
}

