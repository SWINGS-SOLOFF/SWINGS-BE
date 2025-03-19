package com.swings.chat.service;

import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // ✅ 2. 특정 유저가 속한 채팅방 목록 조회
    public List<ChatRoomEntity> getChatRoomsByUser(String userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    // 기존 코드 (채팅방 생성 로직)
    public ChatRoomEntity createOrGetChatRoom(String user1, String user2) {
        return chatRoomRepository.findByUser1AndUser2(user1, user2)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoomEntity(user1, user2)));
    }

}
