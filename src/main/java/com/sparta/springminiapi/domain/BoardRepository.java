package com.sparta.springminiapi.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> { //jpa를 쓰기위해서

    List<Board> findAllByOrderByCreatedAtDesc();
}
