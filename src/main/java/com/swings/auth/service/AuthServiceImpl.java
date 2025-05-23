package com.swings.auth.service;

import com.swings.security.JwtTokenProvider;
import com.swings.security.RefreshTokenEntity;
import com.swings.security.RefreshTokenRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String login(String username, String password, HttpServletResponse response) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        if (!user.isVerified()) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                entity -> entity.setRefreshToken(refreshToken),
                () -> refreshTokenRepository.save(
                        RefreshTokenEntity.builder().user(user).refreshToken(refreshToken).build()
                )
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return accessToken;
    }
}
