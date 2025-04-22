package com.swings.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisherImpl implements RedisPublisher {

    private final RedisTemplate<String, Object> chatRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) //  LocalDateTime 지원
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);//ISO 형식으로 출력

    @Override
    public void publish(ChatMessageDTO message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            String channel = "chat.personal." + message.getRoomId(); // 여기서 자동으로 채널 생성
            chatRedisTemplate.convertAndSend(channel, json);
        } catch (Exception e) {
            log.error("Redis publish 실패", e);
        }
    }
}
