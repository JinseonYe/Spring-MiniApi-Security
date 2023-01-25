package com.sparta.springminiapi.dto;

import com.sparta.springminiapi.domain.Board;
import com.sparta.springminiapi.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String title;
    private final String username;
    private final String content;
    private final List<Comment> commentList;

    public BoardResponseDto(Board board) {
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.commentList = board.getCommentList();
    }
}
