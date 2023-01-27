package com.sparta.springminiapi.dto;

import com.sparta.springminiapi.Enum.StatusEnum;
import lombok.Data;

@Data
public class StatusResponseDto {
    private StatusEnum status;
    private String message;

    public StatusResponseDto(StatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }
}
