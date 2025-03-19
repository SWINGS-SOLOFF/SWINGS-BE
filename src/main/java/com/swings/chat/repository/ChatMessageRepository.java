package com.swings.chat.repository;

import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    // 특정 채팅방의 메시지 목록 조회
    List<ChatMessageEntity> findByChatRoomOrderBySentAtAsc(ChatRoomEntity chatRoom);
}
