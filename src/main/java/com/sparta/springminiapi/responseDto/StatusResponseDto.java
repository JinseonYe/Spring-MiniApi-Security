package com.sparta.springminiapi.responseDto;

import com.sparta.springminiapi.enums.StatusEnum;
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
