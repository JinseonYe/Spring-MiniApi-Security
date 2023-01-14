package com.sparta.springminiapi.domain;

import com.sparta.springminiapi.service.UserService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //회원명, 비밀먼호
    @Column(nullable = false) //null값이 올 수 없음.
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;

    public User(String username, String password, UserRoleEnum userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public boolean isAdmin(){
        return this.userRole == UserRoleEnum.ADMIN; //어떤 역할을 한 건지 모르겠음.
    }
}
