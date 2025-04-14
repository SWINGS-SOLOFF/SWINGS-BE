package com.swings.notification.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMServiceImpl implements FCMService {

    @Override
    public void sendPush(String token, String title, String body) {
        if (token == null || token.isEmpty()) {
            log.warn("❗ FCM 전송 실패: 토큰 없음");
            return;
        }

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("✅ FCM 전송 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("❌ FCM 전송 실패: {}", e.getMessage());
        }
    }
}