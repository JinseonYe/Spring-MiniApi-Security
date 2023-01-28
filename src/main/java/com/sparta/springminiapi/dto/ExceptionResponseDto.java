package com.sparta.springminiapi.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponseDto {
    private final HttpStatus status;
    private final String message;

    public ExceptionResponseDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
