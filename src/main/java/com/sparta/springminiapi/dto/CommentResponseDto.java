package com.sparta.springminiapi.dto;

import com.sparta.springminiapi.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long commentId;
    private String username;
    private String comment;

    public CommentResponseDto(Comment comment) {
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentId = comment.getCommentId();
        this.username = comment.getUsername();
        this.comment = comment.getComment();
    }
}
