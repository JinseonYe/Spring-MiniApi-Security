package com.sparta.springminiapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.springminiapi.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    //연관관계
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    //기본 생성자
    public Comment(String username, String comment, Board board) {
        this.username = username;
        this.comment = comment;
        this.board = board;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
