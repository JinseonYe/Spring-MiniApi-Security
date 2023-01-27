package com.sparta.springminiapi.controller;

import com.sparta.springminiapi.Enum.StatusEnum;
import com.sparta.springminiapi.dto.BoardResponseDto;
import com.sparta.springminiapi.dto.CreateBoardRequestDto;
import com.sparta.springminiapi.dto.StatusResponseDto;
import com.sparta.springminiapi.dto.UpdateBoardRequestDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import com.sparta.springminiapi.service.BoardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    //게시글 생성
    @PostMapping("/api/boards")
    public BoardResponseDto createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto, HttpServletRequest request) { //제이슨이라는 형태로 들어올건데 저 객체에 값좀 넣어줘.
        //Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims; //JWT 안에 들어있는 정보들을 담을 수 있는 객체

        if (token != null) {
            //토큰 유효성 검증, 토큰에서 사용자를 꺼냄. 사용자는 신뢰할 수 있다.
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

//            //토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
//            );

            String username = claims.getSubject(); // DB를 접근하지 않아도 사용자가 맞다고 보고 간다. validateToken

//            //토큰값이 유효한 회원만 게시글 작성 가능.
//            Board board = createBoardRequestDto.toEntity(user.getUsername());
//            boardRepository.save(board);
//            return new BoardResponseDto(board);
//        } else {
//            return null;
//        }

            return boardService.createBoard(createBoardRequestDto, username);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

//            //토큰값이 유효한 회원만 게시글 작성 가능.
//            Board board = createBoardRequestDto.toEntity(user.getUsername());
//            boardRepository.save(board);
//            return new BoardResponseDto(board);
//        } else {
//            return null;
//        }

    //            //토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
//            );

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
    public BoardResponseDto updateBoard(@PathVariable Long boardId, @RequestBody UpdateBoardRequestDto updateBoardRequestDto, HttpServletRequest request) {
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
            String username = claims.getSubject();
            return boardService.updateBoard(boardId, updateBoardRequestDto, username);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    //게시글 삭제
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<StatusResponseDto> deleteBoard(@PathVariable Long boardId, HttpServletRequest request) {
        StatusResponseDto responseDto = new  StatusResponseDto(StatusEnum.OK, "게시글 삭제 완료");
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
            String username = claims.getSubject();
            boardService.deleteBoard(boardId, username);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
