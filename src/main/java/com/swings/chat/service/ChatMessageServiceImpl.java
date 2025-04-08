package com.swings.chat.service;

import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.repository.ChatMessageRepository;
import com.swings.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    // ✅ 특정 채팅방의 모든 메시지 조회
    @Override
    public List<ChatMessageEntity> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoom_RoomIdOrderBySentAtAsc(roomId);
    }

    // ✅ 내가 아닌 메시지를 읽음 처리
    @Override
    public void markMessagesAsRead(Long roomId, String username) {
        List<ChatMessageEntity> unreadMessages = chatMessageRepository.findByChatRoom_RoomIdOrderBySentAtAsc(roomId)
                .stream()
                .filter(msg -> !msg.getSender().equals(username) && !msg.isRead())
                .toList();

        unreadMessages.forEach(msg -> msg.setRead(true));
        chatMessageRepository.saveAll(unreadMessages);
    }
    @Override
    public ChatMessageEntity saveMessage(Long roomId, String sender, String content) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessageEntity message = ChatMessageEntity.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();

        return chatMessageRepository.save(message);
    }

}
