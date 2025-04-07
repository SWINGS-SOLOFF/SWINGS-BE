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

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // ✅ 유저가 속한 채팅방 목록 조회 (마지막 메시지, 시간, 안읽은 수 포함)
    public List<ChatRoomResponseDto> getChatRoomsByUser(String username) {
        // 내가 user1 또는 user2로 포함된 채팅방 전체 가져오기
        List<ChatRoomEntity> rooms = chatRoomRepository.findByUser1OrUser2(username, username);

        return rooms.stream().map(room -> {
            // 채팅방 기준 마지막 메시지 가져오기
            ChatMessageEntity lastMessage = chatMessageRepository.findTopByChatRoom_RoomIdOrderBySentAtDesc(room.getRoomId());

            // 내가 아닌 사람이 보낸 메시지 중 안 읽은 것의 수
            Long unreadCount = chatMessageRepository.countByChatRoom_RoomIdAndSenderNotAndIsReadFalse(
                    room.getRoomId(), username
            );

            return ChatRoomResponseDto.builder()
                    .roomId(room.getRoomId())
                    .user1(room.getUser1())
                    .user2(room.getUser2())
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                    .lastMessageTime(lastMessage != null ? lastMessage.getSentAt() : null)
                    .unreadCount(unreadCount)
                    .build();
        }).collect(Collectors.toList());
    }

    // ✅ 기존 채팅방 있으면 반환, 없으면 새로 생성
    public ChatRoomEntity createOrGetChatRoom(String user1, String user2) {
        return chatRoomRepository.findByUser1AndUser2(user1, user2)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoomEntity(user1, user2)));
    }
}
