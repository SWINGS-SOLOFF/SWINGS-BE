package com.swings.matchgroupchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(Long roomId, ChatMessageDTO message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            String channel = "chat.room." + roomId;
            redisTemplate.convertAndSend(channel, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}