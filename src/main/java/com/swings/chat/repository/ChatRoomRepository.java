package com.swings.chat.repository;

import com.swings.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    List<ChatRoomEntity> findByUser1OrUser2(String user1, String user2);

    // 기존 코드 (채팅방 찾기)
    Optional<ChatRoomEntity> findByUser1AndUser2(String user1, String user2);
}
