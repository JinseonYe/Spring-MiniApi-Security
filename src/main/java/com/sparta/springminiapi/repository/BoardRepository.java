package com.sparta.springminiapi.repository;

import com.sparta.springminiapi.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> { //jpa를 쓰기위해서

    List<Board> findAllByOrderByCreatedAtDesc(); //전체 게시글 목록 리스트

    Optional<Board> findById(Long boardId); //특정 게시글 id값으로 찾기
}
