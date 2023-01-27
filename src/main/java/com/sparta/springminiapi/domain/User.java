package com.sparta.springminiapi.domain;

import com.sparta.springminiapi.Enum.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long UserId;

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

//    public boolean isAdmin(){
//        return this.userRole == UserRoleEnum.ADMIN; // 권한이 어드민이라면
//    }
}
