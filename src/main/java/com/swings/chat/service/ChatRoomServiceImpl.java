package com.swings.chat.service;

import com.swings.chat.dto.ChatRoomResponseDto;
import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.repository.ChatMessageRepository;
import com.swings.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // ✅ 여기만 @Service 붙여야 함
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatRoomResponseDto> getChatRoomsByUser(String username) {
        List<ChatRoomEntity> rooms = chatRoomRepository.findByUser1OrUser2(username, username);

        return rooms.stream().map(room -> {
            ChatMessageEntity lastMessage = chatMessageRepository.findTopByChatRoom_RoomIdOrderBySentAtDesc(room.getRoomId());
            Long unreadCount = chatMessageRepository.countByChatRoom_RoomIdAndSenderNotAndIsReadFalse(room.getRoomId(), username);

            return ChatRoomResponseDto.builder()
                    .roomId(room.getRoomId())
                    .user1(room.getUser1())
                    .user2(room.getUser2())
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                    .lastMessageTime(lastMessage != null ? lastMessage.getSentAt() : null) // 여기 LocalDateTime 그대로 유지
                    .unreadCount(unreadCount)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public ChatRoomEntity createOrGetChatRoom(String user1, String user2) {
        return chatRoomRepository.findByUser1AndUser2(user1, user2)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoomEntity(user1, user2)));
    }
}
