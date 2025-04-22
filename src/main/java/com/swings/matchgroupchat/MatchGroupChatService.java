package com.swings.matchgroupchat;

import com.swings.chat.dto.ChatMessageDTO;

import java.util.List;

public interface MatchGroupChatService {

    void save(ChatMessageDTO dto);

    List<ChatMessageDTO> getMessages(Long matchGroupId);
}
