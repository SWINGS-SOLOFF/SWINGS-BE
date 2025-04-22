package com.swings.chat.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisChatConfig {

    @Bean
    public RedisMessageListenerContainer chatRedisListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisSubscriber chatRedisSubscriber
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatRedisSubscriber, new PatternTopic("chat.personal.*")); // ✅ 여기 채널 중요!
        return container;
    }
}
