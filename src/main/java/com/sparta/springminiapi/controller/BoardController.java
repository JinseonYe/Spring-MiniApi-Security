package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.DeleteBoardRequestDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import com.sparta.springminiapi.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

//    public BoardController(BoardService boardService) { //@RequiredArgsConstructor 로 대체
//        this.boardService = boardService;
//    }

    //게시글 생성
    @PostMapping("/api/boards")
    public void createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto, HttpServletRequest request) { //제이슨이라는 형태로 들어올건데 저 객체에 값좀 넣어줘.
        boardService.createBoard(createBoardRequestDto, request);
    }

    //게시글 조회
    @GetMapping("/api/boards/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    //전체 게시글 조회
    @GetMapping("/api/boards")
    public List<BoardResponseDto> getBoardList() {
        return boardService.getBoardList();
    }

    //게시글 수정
    @PutMapping("/api/boards/{boardId}")
    public void updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardRequestDto updateBoardRequestDto, HttpServletRequest request) {
        boardService.updateBoard(boardId, updateBoardRequestDto, request);
    }

    //게시글 삭제
    @DeleteMapping("/api/boards/{boardId}")
    public void deleteBoard(@PathVariable Long boardId, HttpServletRequest request) {
        boardService.deleteBoard(boardId, request);
    }
}
