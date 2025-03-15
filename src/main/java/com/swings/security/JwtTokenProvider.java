package com.swings.security;

import com.swings.user.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expirationTime; // 설정 파일에서 만료 시간 가져옴

    private Key signingKey;

    // 🔹 @PostConstruct 추가 (Spring이 자동으로 실행하여 JWT 키를 초기화)
    @PostConstruct
    public void init() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            logger.error("🚨 JWT Secret Key가 설정되지 않았습니다! 서버를 종료합니다.");
            throw new IllegalStateException("JWT Secret Key가 설정되지 않았습니다!");
        }
        this.signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 🔹 JWT 생성 (유저네임 + 단일 역할 포함)
    public String generateToken(String username, UserEntity.Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name()) // 🔹 단일 역할만 저장
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 JWT 검증 (위조 검사)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("⚠️ JWT 만료됨: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("⚠️ 지원되지 않는 JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("⚠️ 손상된 JWT: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("⚠️ 서명 검증 실패: {}", e.getMessage());
        } catch (Exception e) {
            logger.warn("⚠️ JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    // 🔹 JWT에서 유저 정보(Username) 가져오기
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            logger.warn("⚠️ JWT에서 사용자 ID 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // 🔹 JWT에서 역할(Role) 정보 가져오기 (단일 값)
    public String extractRole(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return (String) claims.get("role");  // 🔹 단일 값으로 저장했으므로 String으로 반환
        } catch (JwtException e) {
            logger.warn("⚠️ JWT에서 역할 정보 추출 실패: {}", e.getMessage());
            return null;
        }
    }
}
