package com.sparta.springminiapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


//        "title": "스프링최고",
//        "content": "asdasd",
//        "user": "aa",
//        "password":"123"


@Getter
@NoArgsConstructor // 기본생성자 public CreateBoardRequestDto() {} 대신에
public class CreateBoardRequestDto { //dto가 만들어지려면 밑에 애들이 필수로 있어야함을 선언.
    private String title;
    private String user;
    private String password;
    private String content;

    public CreateBoardRequestDto(String title, String user, String password, String content) {
        this.title = title;
        this.user = user;
        this.password = password;
        this.content = content;
    }
}
