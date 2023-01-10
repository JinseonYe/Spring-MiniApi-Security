package com.sparta.springminiapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Board extends TimeStamp{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Long id = 0L;

    //제목, 작성자명, 비밀먼호, 작성 내용
    private Long id;
    private String title;
    private String username;
    private String password;
    private String content;

    public Board(String title, String username, String password, String content) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.content = content;
    }

    //값을 바꾸는 이유: 요구사항에서 바꾸라고 해서.
    public void update(String title, String username, String content) {
        //제목, 작성자명, 작성 내용을 수정
        this.title = title;
        this.username = username;
        this.content = content;
    }

    public boolean isValidPassword(String inputPassword) { //비밀번호 검증 로직
        if (inputPassword.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }
}
