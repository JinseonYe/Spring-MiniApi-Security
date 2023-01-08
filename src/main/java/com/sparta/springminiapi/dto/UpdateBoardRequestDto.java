package com.sparta.springminiapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBoardRequestDto {
    private String title;
    private String username;
    private String content;
    private String password;

}
