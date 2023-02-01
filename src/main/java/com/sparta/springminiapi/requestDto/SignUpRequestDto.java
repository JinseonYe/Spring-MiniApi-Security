package com.sparta.springminiapi.requestDto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자, 숫자로 구성되어야 합니다.") //소문자,숫자,4~10개
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[(?=.*[^\\w\\s])])[A-Za-z\\d(?=.*[^\\w\\s])]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성되어야 합니다.") //소문자, 대문자, 숫자, 특수문자, 8~15개
    private String password;
    private boolean admin = false;
    private String adminToken;
}
