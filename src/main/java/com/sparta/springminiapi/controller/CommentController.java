package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.Enum.StatusEnum;
import com.sparta.springminiapi.dto.CommentRequestDto;
import com.sparta.springminiapi.dto.CommentResponseDto;
import com.sparta.springminiapi.dto.StatusResponseDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import com.sparta.springminiapi.service.CommentService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    //댓글 작성
    @PostMapping("/boards/{boardId}")
    public CommentResponseDto createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //토큰이 유효한지 검사
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            String username = claims.getSubject();

            return commentService.createComment(username, boardId, requestDto);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    //댓글 수정
    @PutMapping("/boards/{boardId}/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //토큰이 유효한지 검사
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            String username = claims.getSubject();

            return commentService.updateComment(username, boardId, commentId, requestDto);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    //댓글 삭제
    @DeleteMapping("/boards/{boardId}/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId, HttpServletRequest request) {
        StatusResponseDto responseDto = new  StatusResponseDto(StatusEnum.OK, "댓글 삭제 완료");
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //토큰이 유효한지 겁사
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            String username = claims.getSubject();
            commentService.deleteComment(boardId, commentId, username);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
