package com.sparta.springminiapi.dto;

import com.sparta.springminiapi.domain.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private final String title;
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
    }
}
