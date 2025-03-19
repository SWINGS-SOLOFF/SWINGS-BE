package com.swings.auth;

import com.swings.security.JwtTokenProvider;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String username, String password) {

        // 🔹 유저 조회 & 비밀번호 검증 (보안 강화)
        UserEntity user = userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword())) // 아이디 + 비밀번호 동시에 검증
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.")); // 보안 강화

        // 🔹 JWT 생성 후 반환
        return jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
    }
}
