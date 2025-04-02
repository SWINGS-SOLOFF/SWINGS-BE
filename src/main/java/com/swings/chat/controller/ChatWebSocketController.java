package com.swings.chat.controller;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    // 클라이언트가 "/app/chat/message"로 메시지 보낼 때 처리
    @MessageMapping("/chat/message")
    public void handleChatMessage(ChatMessageDTO message) {
        log.info("📩 메시지 수신: {}", message);

        // DB 저장
        chatMessageService.saveMessage(message.getRoomId(), message.getSender(), message.getContent());

        // 특정 채팅방 구독자에게 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chat/" + message.getRoomId(), // 예: /topic/chat/2
                message
        );
    }
}
