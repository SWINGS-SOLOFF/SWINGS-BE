package com.swings.chat.service;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.entity.ChatMessageEntity;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageEntity> getMessagesByRoomId(Long roomId);
    void markMessagesAsRead(Long roomId, String username);

    List<ChatMessageDTO> getMessageDTOsByRoomId(Long roomId);

    //  메시지 저장 메서드 추가
    ChatMessageEntity saveMessage(Long roomId, String sender, String content);

    ChatMessageDTO saveAndReturnDTO(Long roomId, String sender, String content);

    }
