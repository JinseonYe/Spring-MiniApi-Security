package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.enums.StatusEnum;
import com.sparta.springminiapi.requestDto.LoginRequestDto;
import com.sparta.springminiapi.requestDto.SignUpRequestDto;
import com.sparta.springminiapi.responseDto.StatusResponseDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import com.sparta.springminiapi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<StatusResponseDto> signUpRequestDto(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        StatusResponseDto responseDto = new  StatusResponseDto(StatusEnum.OK, "회원가입 성공");
        userService.signUp(signUpRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //ajax에 body에 값이 넘어오기 때문에 @RequestBody를 써줘야한다. 클라이언트 쪽에 반환할 때 response 객체를 반환한다.
//    @PostMapping("/api/login")
//    public String signInRequestDto(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
//        return userService.signIn(loginRequestDto, response);
//    }

    @PostMapping("/api/login")
    public ResponseEntity<StatusResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        StatusResponseDto responseDto = new  StatusResponseDto(StatusEnum.OK, "로그인 성공");
        String generatedToken = userService.login(loginRequestDto);
        //헤더에 올리기
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, generatedToken); //대신 ResponseEntity로도 사용 가능

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
