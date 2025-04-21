package com.swings.chat.controller;

import com.swings.chat.dto.ChatRoomResponseDto;
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

    //  1. 특정 유저가 속한 채팅방 목록 조회 API → Dto로 반환
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getRooms(@RequestParam String userId) {
        List<ChatRoomResponseDto> chatRooms = chatRoomService.getChatRoomsByUser(userId);
        return ResponseEntity.ok(chatRooms);
    }

    //  2. 채팅방 생성 or 조회 (기존 그대로 유지)
    @PostMapping("/room")
    public ResponseEntity<ChatRoomEntity> createOrGetChatRoom(
            @RequestParam String user1,
            @RequestParam String user2,
            @RequestParam(required = false, defaultValue = "false") boolean isSuperChat // 추가
    ) {
        ChatRoomEntity chatRoom = chatRoomService.createOrGetChatRoom(user1, user2, isSuperChat); //  수정
        return ResponseEntity.ok(chatRoom);
    }
    @PostMapping("/leave")
    public void leaveRoom(@RequestParam Long roomId, @RequestParam String username) {
        chatRoomService.leaveChatRoom(roomId, username);
    }
}
