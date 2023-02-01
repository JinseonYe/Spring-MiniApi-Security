package com.sparta.springminiapi.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBoardRequestDto {
    private String title;
    private String content;

}
