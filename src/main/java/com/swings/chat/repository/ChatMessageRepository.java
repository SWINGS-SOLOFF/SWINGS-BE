package com.swings.chat.repository;

import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    // ✅ 이거는 ChatRoomEntity 자체로 검색하는 방식
    List<ChatMessageEntity> findByChatRoomOrderBySentAtAsc(ChatRoomEntity chatRoom);

    // ✅ 이거는 ChatRoomEntity 안에 있는 필드인 roomId로 검색하는 방식 (이걸 써야 함)
    List<ChatMessageEntity> findByChatRoom_RoomIdOrderBySentAtAsc(Long roomId);
}
