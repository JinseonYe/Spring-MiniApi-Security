package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.Board;
import com.sparta.springminiapi.domain.BoardRepository;
import com.sparta.springminiapi.domain.UserRepository;
import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service //DB 또는 Controller를 통해서 전달받은 데이터를 가지고 DB나 entity + entity에 있는 행위(UPDATE)들 일을 시킴
@RequiredArgsConstructor
public class BoardService { //컨트롤타워

    private final BoardRepository boardRepository; // BoardRepository에서 상속하면 만들어줘야함. 엔티티처럼
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //게시글 생성 로직
    @Transactional
    public BoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto, String username) {
        //토큰값이 유효한 회원만 게시글 작성 가능.
        Board board = createBoardRequestDto.toEntity(username);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    //게시글 조회
    @Transactional
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("Id값이 일치하는 게시글이 없습니다.")
        );
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    //게시글 전체 조회
    @Transactional
    public List<BoardResponseDto> getBoardList() {
        //작성 날짜 기준 내림차순으로 정렬하기
        List<Board> list = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> totalBoardList = list.stream().map(board -> new BoardResponseDto(board)).collect(Collectors.toList());
        return totalBoardList;
    }

    // 게시글 수정 로직
    @Transactional
    //해당 boardId 게시글이 있는지 확인
    public BoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, String username) {
        //해당 BoardId에 해당하는 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("해당 boardId의 게시글이 존재하지 않습니다.")
        );
            //해당 유저가 작성한 게시글이 맞는지 검사, 맞으면 수정 가능
            //Board한테 시키면 된다. -> Board.isWriter(username); -> 맞으면 수정
            if (board.isWriter(username)) {
                board.update(updateBoardRequestDto.getTitle(), updateBoardRequestDto.getContent());
            } else throw new RuntimeException("본인이 작성한 게시글만 수정할 수 있습니다.");

        //BoardResponseDto 생성 후 리턴
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return boardResponseDto;
    }

    //게시글 삭제 로직
    @Transactional
    public String deleteBoard(Long boardId, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("해당 boardId 의 게시글이 존재하지 않습니다.")
        );
            //해당 유저가 작성한 게시글이 맞는지 검사, 맞으면 삭제 가능
            if (board.isWriter(username)) {
                boardRepository.deleteById(boardId);
            } else throw new RuntimeException("본인이 작성한 게시글만 삭제할 수 있습니다.");

        return "게시글 삭제 성공";
    }
}

