package com.swings.chat.repository;

import com.swings.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    // 이거는 ChatRoomEntity 안에 있는 필드인 roomId로 검색하는 방식 (이걸 써야 함)
    List<ChatMessageEntity> findByChatRoom_RoomIdOrderBySentAtAsc(Long roomId);

    // 채팅방 ID 기준으로 가장 마지막 메시지 1개 (시간 내림차순 후 1개만)
    ChatMessageEntity findTopByChatRoom_RoomIdOrderBySentAtDesc(Long roomId);

    // 내가 보낸 메시지가 아닌 것 중 아직 읽지 않은 메시지 수
    Long countByChatRoom_RoomIdAndSenderNotAndIsReadFalse(Long roomId, String username);

}
