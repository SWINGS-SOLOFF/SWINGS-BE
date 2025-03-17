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
        // 🔹 먼저 유저 조회 (아이디가 틀리면 즉시 예외 발생)
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 🔹 비밀번호 검증 (틀리면 예외 발생)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        // 🔹 JWT 생성 후 반환
        return jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
    }
}
