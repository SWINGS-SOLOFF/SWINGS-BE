package com.swings.chat.service;

import com.swings.chat.dto.ChatRoomResponseDto;
import com.swings.chat.entity.ChatRoomEntity;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomResponseDto> getChatRoomsByUser(String username);

    ChatRoomEntity createOrGetChatRoom(String user1, String user2);
}

