package com.ssafy.pjt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Claims;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME; // 1시간
    
    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
    	this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    

    // JWT 생성
    public String createToken(Authentication authentication) {
    	
    	log.debug("authentication.name : {}", authentication.getName());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Key 객체 사용
                .compact();
    }

    // JWT에서 클레임 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()    // parserBuilder() 사용
                .setSigningKey(secretKey)    // setSigningKey을 builder에서 설정
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
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
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);  // subject로부터 userId 추출
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}