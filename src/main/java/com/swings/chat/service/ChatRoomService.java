package com.swings.chat.service;

import com.swings.chat.dto.ChatRoomResponseDto;
import com.swings.chat.entity.ChatRoomEntity;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomResponseDto> getChatRoomsByUser(String username);

    ChatRoomEntity createOrGetChatRoom(String user1, String user2);

    //  슈퍼챗 구분용 메서드 추가
    ChatRoomEntity createOrGetChatRoom(String user1, String user2, boolean isSuperChat);
    void leaveChatRoom(Long roomId, String username); // 사용자 퇴장 처리
}
