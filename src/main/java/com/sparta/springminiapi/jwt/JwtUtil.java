package com.sparta.springminiapi.jwt;

import com.sparta.springminiapi.domain.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    //토큰 생성에 필요한 값
    public static final String AUTHORIZATION_HEADER = "Authorization"; //Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth"; //사용자 권한 값의 KEY
    private static final String BEARER_PREFIX = "Bearer "; //TOKEN 식별자
    private static final long TOKEN_TIME = 60 * 60 * 1000L; //TOKEN 만료시간 (1시간)

    @Value("${jwt.secret.key}")
    private String secretKey; //application.properties에 넣어준 값을 가져온다.
    private Key key; //토큰을 만들 때 넣어줄 키 값
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct //객체가 생성될 때 초기화하는 함수
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //Base64로 인코딩되어있는 secretKey를 가져와서 디코딩하는 것.
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header에서 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) { //HttpServletRequest 안에 우리가 가져와야 될 토큰이 header에 들어있음.
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //AUTHORIZATION_HEADER에 들어있는 토큰값을 가져오는 코드
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { //그렇게 가져온 코드가 있는지, bearer로 시작하는지 확인
            return bearerToken.substring(7); //토큰과 연관되지 않은 string을 떼어낸 후 반환.
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum userRole) { //string 형식의 JWT 토큰으로 반환된다.
        Date date = new Date();

        return BEARER_PREFIX + //토큰 앞에 bearer이 붙어서
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, userRole)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //지금 기준으로 토큰을 언제까지 유효하게 가져갈건지 지정
                        .setIssuedAt(date) //토큰이 언제 만들어졌는지
                        .signWith(key, signatureAlgorithm) //어떤 알고리즘으로 암호화할지
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) { //토큰을 만들 때 사용한 키를 넣어주고 어떤 토큰을 검증할건지 넣어준 후, try catch로 잡아준다.
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) { //이미 앞에서 validateToken으로 검증하고 유효한 토큰임을 가정했기때문에 try catch가 없는 것.
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); //getBody를 통해 그 안에 들어있는 정보를 가져옴.
    }
}
