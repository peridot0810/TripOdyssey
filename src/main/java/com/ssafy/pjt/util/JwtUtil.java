package com.ssafy.pjt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME; // 1시간

    // JWT 생성
    public String createToken(String userId) {
    	// SECRET_KEY를 SecretKey 객체로 변환
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)  // Key 객체 사용
                .compact();
    }

    // JWT에서 클레임 추출
    public Claims extractClaims(String token) {
    	// SecretKey 객체로 변환
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.parserBuilder()    // parserBuilder() 사용
                .setSigningKey(key)    // setSigningKey을 builder에서 설정
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 유저 아이디 추출
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // 토큰이 유효한지 체크
    public boolean validateToken(String token, String userId) {
        return (userId.equals(extractUserId(token)) && !isTokenExpired(token));
    }
}