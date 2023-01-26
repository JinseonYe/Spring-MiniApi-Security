package com.sparta.springminiapi.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends TimeStamp{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;

    //연관관계
    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

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
    public void update(String title, String content) {
        //제목, 작성 내용을 수정
        this.title = title;
        this.content = content;
    }

    public boolean isWriter(String username) {
        return this.username.equals(username); //if(board.getUsername().equals(user.getUsername())); 보드서비스에 있던 애를 엔티티에서 일 시키기
    }

}
