package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.Board;
import com.sparta.springminiapi.domain.BoardRepository;
import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.DeleteBoardRequestDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//게시글 작성
@Service //DB 또는 Controller를 통해서 전달받은 데이터를 가지고 DB나 entity + entity에 있는 행위(UPDATE)들 일을 시킴.
public class BoardService { //컨트롤타워

    private final BoardRepository boardRepository; // BoardRepository에서 상속하면 만들어줘야함. 엔티티처럼

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //게시글 생성 로직
    @Transactional
    public void createBoard(CreateBoardRequestDto createBoardRequestDto) { //컨트롤러로부터 사용자가 요청한 요청정보들을 전달받아서 entity를 만들고 레파지토리를 통해서 디비한테 저장하라고 시킨다.
        Board board = new Board(createBoardRequestDto.getTitle(), createBoardRequestDto.getUsername(), createBoardRequestDto.getPassword(), createBoardRequestDto.getContent());
        boardRepository.save(board);
    }

    //게시글 조회 로직
    @Transactional
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
        return new BoardResponseDto(board);
    }

    //게시글 전체 조회
    @Transactional
    public List<BoardResponseDto> getBoardList() {
        //작성 날짜 기준 내림차순으로 정렬하기
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return boardResponseDtoList;
    }

    // 게시글 수정 로직
    @Transactional
    public void updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto) {
        Board boardSaved = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
        // 수정을 요청할 때 수정할 데이터와 비밀번호를 같이 보내서 서버에서 비밀번호 일치 여부를 확인 한 후 업데이트해라.
        if (boardSaved.isValidPassword(updateBoardRequestDto.getPassword())) {
            boardSaved.update(updateBoardRequestDto.getTitle(),updateBoardRequestDto.getUsername(),updateBoardRequestDto.getContent());
            boardRepository.save(boardSaved); //대신에 @Transactional 가능
        } else {
            throw new IllegalArgumentException("패스워드가 틀렸습니다.");
        }
    }

    //게시글 삭제 로직
    @Transactional
    public void deleteBoard(Long boardId, DeleteBoardRequestDto deleteBoardRequestDto) {
        Board boardDelete = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
        String password = deleteBoardRequestDto.getPassword();
        if (boardDelete.isValidPassword(password)) {
            boardRepository.delete(boardDelete);
            System.out.println("삭제 완료");
        } else {
            throw new IllegalArgumentException("패스워드가 틀렸습니다.");
        }
    }
}

