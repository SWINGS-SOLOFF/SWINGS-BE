package com.swings.chat.redis;

import com.swings.chat.dto.ChatMessageDTO;

public interface RedisPublisher {  // 친구 스타일
    void publish(ChatMessageDTO message);           // 내 스타일
}
