package com.sparta.springminiapi.service;

import com.sparta.springminiapi.enums.UserRoleEnum;
import com.sparta.springminiapi.domain.*;
import com.sparta.springminiapi.responseDto.BoardResponseDto;
import com.sparta.springminiapi.requestDto.CreateBoardRequestDto;
import com.sparta.springminiapi.requestDto.UpdateBoardRequestDto;
import com.sparta.springminiapi.repository.BoardRepository;
import com.sparta.springminiapi.repository.UserRepository;
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
    public BoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, String username) {
        //해당 BoardId에 해당하는 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 boardId의 게시글이 존재하지 않습니다.")
        );

        //토큰 값이 동일한 유저인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저의 게시글이 존재하지 않습니다.")
        );

        UserRoleEnum userRoleEnum = user.getUserRole();
        if (!user.getUsername().equals(board.getUsername()) && userRoleEnum == UserRoleEnum.USER) { //유저네임이 일치하지 않는 유저일 때 제외하고 모두 게시글 작성 가능.
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        board.update(updateBoardRequestDto.getTitle(), updateBoardRequestDto.getContent());

        //BoardResponseDto 생성 후 리턴
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return boardResponseDto;
    }

    //게시글 삭제 로직
    @Transactional
    public void deleteBoard(Long boardId, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("해당 boardId 의 게시글이 존재하지 않습니다.")
        );

        User user = userRepository.findByUsername(username).orElseThrow( //이 로직은 아래에서 처리해주기 때문에 없어도 될 거 같은데
                () -> new RuntimeException("해당 유저의 게시글이 존재하지 않습니다.")
        );

        UserRoleEnum userRoleEnum = user.getUserRole();
        if (!user.getUsername().equals(board.getUsername()) && userRoleEnum == UserRoleEnum.USER) { //유저네임이 일치하지 않는 유저일 때 제외하고 모두 게시글 작성 가능.
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        boardRepository.deleteById(boardId);
    }
}

