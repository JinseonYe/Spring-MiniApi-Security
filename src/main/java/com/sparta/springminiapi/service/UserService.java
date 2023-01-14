package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.User;
import com.sparta.springminiapi.domain.UserRepository;
import com.sparta.springminiapi.dto.LoginRequestDto;
import com.sparta.springminiapi.dto.SignUpRequestDto;
import com.sparta.springminiapi.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor //생성자 주입
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional //회원가입
    public void signUp(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        String password = signUpRequestDto.getPassword();

        //회원명 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        //유저 생성 후 DB에 저장
        User user = new User(username, password);
        userRepository.save(user);
    }

    @Transactional //로그인
    public String login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        //회원 유무 확인
        User user = userRepository.findByUsername(username). orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 비교
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String generatedToken = jwtUtil.createToken(user.getUsername());

        return generatedToken;
    }
}
