package com.swings.security;

import com.swings.user.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret-file}")
    private Resource secretKeyResource;  // classpath 또는 file 경로 지원

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key signingKey;

    @PostConstruct
    public void init() {
        try {
            logger.info("JWT 키 파일 로드 중: {}", secretKeyResource.getFilename());

            // 파일을 InputStream으로 읽고 문자열로 변환
            String secretKey = new String(secretKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();

            if (secretKey.isEmpty()) {
                throw new IllegalStateException("JWT Secret Key가 비어있습니다.");
            }

            this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            logger.info("JWT Secret Key 초기화 완료");

        } catch (Exception e) {
            logger.error("JWT SecretKey 파일 로드 실패: {}", e.getMessage());
            throw new RuntimeException("JWT SecretKey 파일 읽기 실패", e);
        }
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

    public String generateRefreshToken(String username) {
        long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L; // 7일
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT 만료됨: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("지원되지 않는 JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("손상된 JWT: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("서명 검증 실패: {}", e.getMessage());
        } catch (Exception e) {
            logger.warn("JWT 검증 실패: {}", e.getMessage());
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
