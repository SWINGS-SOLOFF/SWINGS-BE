package com.swings.chat.controller;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.service.ChatMessageService;
import com.swings.chat.redis.RedisPublisher; // ✅ import 경로도 너의 chat.redis 기준으로 수정
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat/message")
    public void handleChatMessage(ChatMessageDTO message) {
        log.info(" WebSocket 메시지 수신: {}", message);

        // 1. DB 저장
        ChatMessageDTO saved = chatMessageService.saveAndReturnDTO(
                message.getRoomId(),
                message.getSender(),
                message.getContent()
        );

        // 2. Redis 발행 (채널명은 내부에서 자동 처리됨)
        redisPublisher.publish(saved);

        log.info(" Redis 발행 완료: {}", saved.getRoomId());
    }
}
