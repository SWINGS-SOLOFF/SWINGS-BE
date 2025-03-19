package com.swings.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.chat.entity.ChatMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPubService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String topic, ChatMessageEntity chatMessage){
        try {
            String message = objectMapper.writeValueAsString(chatMessage);
            stringRedisTemplate.convertAndSend(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
