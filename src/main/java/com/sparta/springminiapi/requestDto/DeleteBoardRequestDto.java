package com.sparta.springminiapi.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteBoardRequestDto {

    private String password;

    public DeleteBoardRequestDto(String password) {
        this.password = password;
    }
}
