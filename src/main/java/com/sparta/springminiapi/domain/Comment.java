package com.sparta.springminiapi.domain;

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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    //기본 생성자
    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
