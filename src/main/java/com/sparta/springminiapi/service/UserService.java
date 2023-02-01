package com.sparta.springminiapi.service;

import com.sparta.springminiapi.domain.User;
import com.sparta.springminiapi.repository.UserRepository;
import com.sparta.springminiapi.enums.UserRoleEnum;
import com.sparta.springminiapi.requestDto.LoginRequestDto;
import com.sparta.springminiapi.requestDto.SignUpRequestDto;
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

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"; // ADMIN_TOKEN

    @Transactional //회원가입
    public void signUp(SignUpRequestDto signUpRequestDto) {
        String username = signUpRequestDto.getUsername();
        String password = signUpRequestDto.getPassword();

        //회원명 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        //회원 권한 확인
        UserRoleEnum userRole = UserRoleEnum.USER;
        if (signUpRequestDto.isAdmin()) { //가입을 요청하는 회원이 관리자라면
            if (!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            userRole = UserRoleEnum.ADMIN;
        }

//        if (signUpRequestDto.getAdminToken() != null) signUpRequestDto.setAdmin(true);
//        if (signUpRequestDto.isAdmin()) {
//            if (!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
//                throw new IllegalArgumentException("관리자 암호가 일치하지 않아 등록할 수 없습니다.");
//            }
//            userRole = UserRoleEnum.ADMIN;
//        }

            //유저 생성 후 DB에 저장
            User user = new User(username, password, userRole);
            userRepository.save(user);
        }

    @Transactional //로그인
    public String login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        //회원 유무 확인
        User user = userRepository.findByUsername(username). orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );

        //비밀번호 비교
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        String generatedToken = jwtUtil.createToken(user.getUsername(),user.getUserRole());

        return generatedToken;
    }
}
