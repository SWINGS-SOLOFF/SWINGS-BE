package com.swings.chat.controller;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller  // WebSocket 전용 컨트롤러
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;

    // WebSocket 메시지 처리
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        // 메시지 저장 (WebSocket에서 메시지 저장)
        System.out.println("Received Message: " + message.getContent()); // 로그 출력

        chatMessageService.saveMessage(message.getRoomId(), message.getSender(), message.getContent());

        // 클라이언트에게 메시지 전송
        return message;
    }
}
