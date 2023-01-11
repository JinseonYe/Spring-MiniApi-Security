package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.dto.LoginRequestDto;
import com.sparta.springminiapi.dto.SignUpRequestDto;
import com.sparta.springminiapi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public String signUpRequestDto(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return "회원가입 완료";
    }

    //ajax에 body에 값이 넘어오기 때문에 @RequestBody를 써줘야한다. 클라이언트 쪽에 반환할 때 response 객체를 반환한다.
    @PostMapping("/api/login")
    public String signInRequestDto(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.signIn(loginRequestDto, response);
        return "로그인 완료";
    }

}
