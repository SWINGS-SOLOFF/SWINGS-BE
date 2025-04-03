package com.swings.security;

import com.swings.user.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret-file}")
    private String secretKeyFile; // 🔑 설정파일에서 파일 경로 받기

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key signingKey;

    // ✅ 파일에서 JWT SecretKey 로드
    private String loadSecretKeyFromFile(String filePath) {
        try {
            logger.info("🔐 JWT 키 파일 로드 중: {}", filePath);
            return Files.readString(Paths.get(filePath)).trim();
        } catch (Exception e) {
            logger.error("🚨 JWT SecretKey 파일 로드 실패: {}", e.getMessage());
            throw new RuntimeException("JWT SecretKey 파일 읽기 실패", e);
        }
    }

    @PostConstruct
    public void init() {
        String secretKey = loadSecretKeyFromFile(secretKeyFile);
        if (secretKey == null || secretKey.isEmpty()) {
            logger.error("🚫 JWT Secret Key가 비어있음! 서버 종료");
            throw new IllegalStateException("JWT Secret Key가 설정되지 않았습니다!");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        logger.info("✅ JWT Secret Key 초기화 완료");
    }

    public String generateToken(String username, UserEntity.Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

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

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }
}
