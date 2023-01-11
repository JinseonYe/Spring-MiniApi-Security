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
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    public Board(String title, String username, String content) {
        this.title = title;
        this.username = username;
        this.content = content;
    }

    //값을 바꾸는 이유: 요구사항에서 바꾸라고 해서.
    public void update(String title, String username, String content) {
        //제목, 작성자명, 작성 내용을 수정
        this.title = title;
        this.username = username;
        this.content = content;
    }
}
