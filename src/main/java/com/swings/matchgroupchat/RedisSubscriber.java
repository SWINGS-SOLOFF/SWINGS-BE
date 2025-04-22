package com.swings.matchgroupchat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchGroupChatService matchGroupChatService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msgBody = new String(message.getBody());
            ChatMessageDTO chatMessage = objectMapper.readValue(msgBody, ChatMessageDTO.class);

            matchGroupChatService.save(chatMessage);

            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatMessage.getRoomId(),
                    chatMessage
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}