package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.DeleteBoardRequestDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import com.sparta.springminiapi.service.BoardService;
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

    //
    @PostMapping("/api/boards") // Create Posts -> 게시물을 생성한다. 게시물 생성 API
    public void createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto) { //제이슨이라는 형태로 들어올건데 저 객체에 값좀 넣어줘.
        boardService.createBoard(createBoardRequestDto);
    }

    @GetMapping("/api/boards/{boardId}") //게시글 조회
    public BoardResponseDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    @GetMapping("/api/boards") //전체 게시글 조회
    public List<BoardResponseDto> getBoardList() {
        return boardService.getBoardList();
    }

    @PutMapping("/api/boards/{boardId}") //게시글 수정
    public void updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardRequestDto updateBoardRequestDto) {
        boardService.updateBoard(boardId, updateBoardRequestDto);
    }

    @DeleteMapping("boards/{boardId}") //게시글 삭제
    public void deleteBoard(@PathVariable Long boardId, @RequestBody DeleteBoardRequestDto deleteBoardRequestDto) {
        boardService.deleteBoard(boardId, deleteBoardRequestDto);
    }
}
