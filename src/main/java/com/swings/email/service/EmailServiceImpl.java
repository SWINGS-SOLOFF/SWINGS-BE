package com.swings.email.service;

import com.swings.email.entity.UserVerifyEntity;
import com.swings.email.entity.UserVerifyEntity.VerifyType;
import com.swings.email.repository.UserVerifyRepository;
import com.swings.user.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);

        UserVerifyEntity verification = UserVerifyEntity.builder()
                .user(user)
                .token(token)
                .type(VerifyType.EMAIL_VERIFY)
                .expiryDate(expiry)
                .used(false)
                .build();

        tokenRepository.save(verification);

        String link = "http://localhost:8090/swings/email/verify?token=" + token;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("[SWINGS] 이메일 인증을 완료해주세요");

            String html = """
                <html>
                  <body style="font-family: 'Segoe UI', sans-serif; background-color: #f7f7f7; padding: 40px;">
                    <div style="max-width: 500px; margin: auto; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                      <h2 style="text-align: center; color: #2b2d42;">SWINGS</h2>
                      <p style="text-align: center; color: #8d99ae; font-size: 16px; margin-bottom: 30px;">나랑 골프치러 갈래?</p>
                      
                      <p style="font-size: 16px; color: #333;">안녕하세요 <strong>%s</strong>님,</p>
                      <p style="font-size: 15px; color: #444;">이메일 주소 인증을 위해 아래 버튼을 클릭해주세요.</p>
                      
                      <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="display: inline-block; background-color: #2b2d42; color: white; padding: 12px 24px; border-radius: 6px; text-decoration: none; font-weight: bold;">
                          이메일 인증하기
                        </a>
                      </div>
                      
                      <p style="font-size: 13px; color: #888;">본 이메일 인증 링크는 <strong>24시간</strong> 동안만 유효합니다.<br/>
                      인증을 완료하지 않으면 로그인이 제한됩니다.</p>

                      <hr style="margin-top: 40px; border: none; border-top: 1px solid #eee;" />
                      <p style="text-align: center; font-size: 12px; color: #aaa; margin-top: 20px;">
                        이 메일은 SWINGS 시스템에 의해 자동 발송되었습니다.<br/>문의: swingsproject@gmail.com
                      </p>
                    </div>
                  </body>
                </html>
                """.formatted(user.getUsername(), link);

            helper.setText(html, true); // HTML 메일
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }
    }
    @Override
    public void sendTemporaryPassword(UserEntity user, String tempPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("[SWINGS] 임시 비밀번호 안내");

            String html = """
                <html>
                  <body style="font-family: 'Segoe UI', sans-serif; background-color: #f7f7f7; padding: 40px;">
                    <div style="max-width: 500px; margin: auto; background-color: #ffffff; padding: 36px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                      <h2 style="text-align: center; color: #2b2d42;">SWINGS</h2>
                      <p style="text-align: center; color: #8d99ae;">임시 비밀번호 안내</p>
                      <p>안녕하세요 <strong>%s</strong>님,</p>
                      <p>아래 임시 비밀번호로 로그인한 후 비밀번호를 꼭 변경해주세요.</p>
                      <div style="text-align: center; margin: 30px 0;">
                        <span style="display: inline-block; padding: 12px 24px; background-color: #2b2d42; color: white; border-radius: 5px; font-size: 18px;">
                          %s
                        </span>
                      </div>
                      <p style="font-size: 13px; color: #999;">이 비밀번호는 보안을 위해 1회용이며, 재사용하지 마세요.</p>
                    </div>
                  </body>
                </html>
                """.formatted(user.getUsername(), tempPassword);

            helper.setText(html, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("임시 비밀번호 메일 전송 실패", e);
        }
    }

}
