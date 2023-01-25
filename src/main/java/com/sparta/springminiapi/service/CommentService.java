package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.*;
import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CommentRequestDto;
import com.sparta.springminiapi.dto.CommentResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //댓글 작성
    @Transactional
    public CommentResponseDto createComment(String username, Long boardId, CommentRequestDto requestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        Comment comment = new Comment(username, requestDto.getComment());
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(String username, Long boardId, Long commentId, CommentRequestDto requestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (comment.getUsername().equals(user.getUsername())) {
            comment.update(requestDto);
        } else throw new RuntimeException("본인이 작성한 댓글만 수정할 수 있습니다.");

        return new CommentResponseDto(comment);

//        return new CommentResponseDto(comment);
            //위 아래 로직이 서로 어떻게 다른지?
//        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//        return commentResponseDto;
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (comment.getUsername().equals(user.getUsername())) {
            commentRepository.deleteById(commentId);
        } else throw new RuntimeException("본인이 작성한 댓글만 삭제할 수 있습니다.");
    }
}
