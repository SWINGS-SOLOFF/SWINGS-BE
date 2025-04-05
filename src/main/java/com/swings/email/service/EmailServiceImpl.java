package com.swings.email.service;

import com.swings.email.entity.UserVerifyEntity;
import com.swings.email.entity.UserVerifyEntity.VerifyType;
import com.swings.email.repository.UserVerifyRepository;
import com.swings.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserVerifyRepository tokenRepository;

    private static final int TOKEN_EXPIRY_HOURS = 24;

    @Override
    @Transactional
    public void sendEmailVerification(UserEntity user) {
        // 1. 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);

        // 2. DB에 저장
        UserVerifyEntity verification = UserVerifyEntity.builder()
                .user(user)
                .token(token)
                .type(VerifyType.EMAIL_VERIFY)
                .expiryDate(expiry)
                .used(false)
                .build();

        tokenRepository.save(verification);

        // 3. 이메일 전송
        String link = "http://localhost:8090/swings/email/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("[SWINGS] 이메일 인증을 완료해주세요");
        message.setText("아래 링크를 클릭하면 인증이 완료됩니다:\n\n" + link);
        mailSender.send(message);
    }
}
