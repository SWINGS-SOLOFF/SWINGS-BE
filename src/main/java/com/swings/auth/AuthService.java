package com.swings.auth;

import com.swings.security.JwtTokenProvider;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String username, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new IllegalArgumentException("잘못된 사용자 정보입니다.");
        }

        UserEntity user = optionalUser.get();
        return jwtTokenProvider.generateToken(user.getUsername(), user.getRole()); // 🔹 JWT 발급
    }
}
