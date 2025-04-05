package com.swings.email.controller;

import com.swings.email.entity.UserVerifyEntity;
import com.swings.email.repository.UserVerifyRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final UserVerifyRepository tokenRepository;
    private final UserRepository userRepository;

    @GetMapping("/verify")
    @Transactional
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<UserVerifyEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("유효하지 않은 인증 링크입니다.");
        }

        UserVerifyEntity verification = optionalToken.get();

        if (verification.isExpired()) {
            return ResponseEntity.badRequest().body("인증 링크가 만료되었습니다.");
        }

        if (verification.isUsed()) {
            return ResponseEntity.badRequest().body("이미 사용된 인증 링크입니다.");
        }

        // 인증 처리
        UserEntity user = verification.getUser();
        user.setVerified(true);
        userRepository.save(user);

        verification.setUsed(true);
        tokenRepository.save(verification);

        return ResponseEntity.ok("이메일 인증이 완료되었습니다. 이제 로그인할 수 있습니다.");
    }
}
