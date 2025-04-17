package com.swings.chat.controller;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")  // HTTP API만 담당
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    // HTTP POST 요청으로 메시지 저장
    @PostMapping("/message")
    public ResponseEntity<String> saveMessage(@RequestBody ChatMessageDTO message) {
        chatMessageService.saveMessage(message.getRoomId(), message.getSender(), message.getContent());
        return ResponseEntity.ok("Message saved successfully!");
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        List<ChatMessageDTO> messages = chatMessageService.getMessageDTOsByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/messages/read")
    public ResponseEntity<Void> markMessagesAsRead(
            @RequestParam Long roomId,
            @RequestParam String username
    ) {
        chatMessageService.markMessagesAsRead(roomId, username);
        return ResponseEntity.ok().build();
    }

}
