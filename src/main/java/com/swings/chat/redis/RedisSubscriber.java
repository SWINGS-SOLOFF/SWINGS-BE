package com.swings.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("chatRedisSubscriber")  // 친구꺼랑 이름 충돌 방지!
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msgBody = new String(message.getBody());
            ChatMessageDTO chatMessage = objectMapper.readValue(msgBody, ChatMessageDTO.class);
            log.info("📥 [chat.personal.*] Redis 수신 완료: {}", chatMessage);

            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatMessage.getRoomId(),
                    chatMessage
            );
        } catch (Exception e) {
            log.error("❌ [chatRedisSubscriber] 처리 실패", e);
        }
    }
}
