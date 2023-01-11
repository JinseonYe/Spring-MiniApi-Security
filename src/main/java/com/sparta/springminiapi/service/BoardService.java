package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.Board;
import com.sparta.springminiapi.domain.BoardRepository;
import com.sparta.springminiapi.domain.User;
import com.sparta.springminiapi.domain.UserRepository;
import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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

    //@RequiredArgsConstructor로 대체
//    public BoardService(BoardRepository boardRepository, UserRepository userRepository, JwtUtil jwtUtil) {
//        this.boardRepository = boardRepository;
//        this.userRepository = userRepository;
//        this.jwtUtil = jwtUtil;
//    }

    //게시글 생성 로직
    @Transactional
    public BoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto, HttpServletRequest request) {
        //Request 에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        //토큰이 있는 경우에만 게시글 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            //토큰에서 가져온 사용자 정보로 DB에서 해당유저를 찾아서 사용할 유저 객체 생성
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
            );
            //토큰값이 유효한 회원만 게시글 작성 가능.
            Board board = createBoardRequestDto.toEntity(user.getUsername());
            boardRepository.save(board);
            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }

    //jwt 이전
//    public void createBoard(CreateBoardRequestDto createBoardRequestDto) { //컨트롤러로부터 사용자가 요청한 요청정보들을 전달받아서 entity를 만들고 레파지토리를 통해서 디비한테 저장하라고 시킨다.
//        Board board = new Board(createBoardRequestDto.getTitle(), createBoardRequestDto.getUsername(), createBoardRequestDto.getPassword(), createBoardRequestDto.getContent());
//        boardRepository.save(board);
//    }

    //게시글 조회
    @Transactional
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("Id값이 일치하는 게시글이 없습니다.")
        );
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    //jwt 이전
//    public BoardResponseDto getBoard(Long boardId) {
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
//        return new BoardResponseDto(board);
//    }

    //게시글 전체 조회
    @Transactional
    public List<BoardResponseDto> getBoardList() {
        //작성 날짜 기준 내림차순으로 정렬하기
        List<Board> list = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> totalBoardList = list.stream().map(board -> new BoardResponseDto(board)).collect(Collectors.toList());
        return totalBoardList;
    }

    //jwt 이전
//    public List<BoardResponseDto> getBoardList() {
//
//        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
//        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
//        for (Board board : boardList) {
//            boardResponseDtoList.add(new BoardResponseDto(board));
//        }
//        return boardResponseDtoList;
//    }

    // 게시글 수정 로직
    @Transactional
    //해당 boardId 게시글이 있는지 확인
    public BoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("해당 boardId의 게시글이 존재하지 않습니다.")
        );

        //Request 에서 토큰 가져오기
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

            //토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
            );

            //해당 유저가 작성한 게시글이 맞는지 겁사, 맞으면 수정 가능
            if (board.getUsername().equals(user.getUsername())) {
                board.update(updateBoardRequestDto.getTitle(), updateBoardRequestDto.getUsername(), updateBoardRequestDto.getContent());
            } else throw new RuntimeException("본인이 작성한 게시글만 수정할 수 있습니다.");
        } else {
            return null;
        }
        //BoardResponseDto 생성 후 리턴
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return boardResponseDto;
    }

    //jwt 이전
//    public void updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto) {
//        Board boardSaved = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
//        // 수정을 요청할 때 수정할 데이터와 비밀번호를 같이 보내서 서버에서 비밀번호 일치 여부를 확인 한 후 업데이트해라.
//        if (boardSaved.isValidPassword(updateBoardRequestDto.getPassword())) {
//            boardSaved.update(updateBoardRequestDto.getTitle(),updateBoardRequestDto.getUsername(),updateBoardRequestDto.getContent());
//            boardRepository.save(boardSaved); //대신에 @Transactional 가능
//        } else {
//            throw new IllegalArgumentException("패스워드가 틀렸습니다.");
//        }
//    }

    //게시글 삭제 로직
    @Transactional
    public String deleteBoard(Long boardId, HttpServletRequest request) {
        //해당 BoardId에 해당하는 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("해당 boardId 의 게시글이 존재하지 않습니다.")
        );
        //Request에서 토큰 가져오기
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
            //토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
            );

            //5. 해당 유저가 작성한 게시글이 맞는지 검사, 맞으면 삭제 가능
            if (board.getUsername().equals((user.getUsername()))) {
                boardRepository.deleteById(boardId);
            } else throw new RuntimeException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }else {
            return null;
        }
        return "게시글 삭제 성공";
    }

    //jwt 이전
//    public void deleteBoard(Long boardId, DeleteBoardRequestDto deleteBoardRequestDto) {
//        Board boardDelete = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
//        String password = deleteBoardRequestDto.getPassword();
//        if (boardDelete.isValidPassword(password)) {
//            boardRepository.delete(boardDelete);
//            System.out.println("삭제 완료");
//        } else {
//            throw new IllegalArgumentException("패스워드가 틀렸습니다.");
//        }
//    }
}

