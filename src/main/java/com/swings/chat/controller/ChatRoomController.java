package com.swings.chat.controller;

import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // ✅ 1. 특정 유저가 속한 채팅방 목록 조회 API
    @GetMapping("/rooms")
    public List<ChatRoomEntity> getRooms(@RequestParam String userId) {
        return chatRoomService.getChatRoomsByUser(userId); // ✅ 자동으로 JSON으로 직렬화됨
    }

    // 기존 코드 (채팅방 생성 API)
    @PostMapping("/room")
    public ResponseEntity<ChatRoomEntity> createOrGetChatRoom(@RequestParam String user1, @RequestParam String user2) {
        ChatRoomEntity chatRoom = chatRoomService.createOrGetChatRoom(user1, user2);
        return ResponseEntity.ok(chatRoom);
    }
}
