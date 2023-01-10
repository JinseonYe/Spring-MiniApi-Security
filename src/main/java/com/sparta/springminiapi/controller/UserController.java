package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.dto.SignUpRequestDto;
import com.sparta.springminiapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public String signUpRequestDto(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return "회원가입 완료";
    }
}
