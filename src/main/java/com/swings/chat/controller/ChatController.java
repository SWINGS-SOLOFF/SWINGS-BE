package com.swings.chat.controller;

import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.service.ChatPubService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    // 그룹 채팅
    private final ChatPubService chatPubService;

    @MessageMapping("/sendMessage")
    @SendTo("/sub/chat/room/{roomId}")
    public void sendMessage(@RequestBody ChatMessageEntity chatMessageEntity){
        chatPubService.publish("chatroom", chatMessageEntity);
    }
}
