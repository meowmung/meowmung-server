package com.example.member.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {
    private final long expiration;
    private final SecretKey key;

    // JWT 기본값
    public JwtUtils(
            @Value("${jwt.expiration}") long expiration,
            @Value("${jwt.secret}") String secret) {
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // JWT 생성하기
    public String generateToken(String email, String nickname){
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .subject(email)
                .expiration(expirationDate)
                .claim("nickname", nickname)
                .signWith(key)
                .compact();
    }

    // JWT에서 정보 가져오기 -> HEADER, PAYLOAD, VERIFY SIGNATURE
    public String parseToken(String token){
        Claims payload = (Claims) Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token)
                .getPayload();
        return payload.getSubject();
    }
}
