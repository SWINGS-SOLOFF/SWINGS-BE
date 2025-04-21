package com.swings.chat.service;

import com.swings.chat.dto.ChatRoomResponseDto;
import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import com.swings.user.entity.UserEntity;
import com.swings.chat.repository.ChatMessageRepository;
import com.swings.chat.repository.ChatRoomRepository;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatRoomResponseDto> getChatRoomsByUser(String username) {
        List<ChatRoomEntity> rooms = chatRoomRepository.findByUser1OrUser2(username, username);

        return rooms.stream()
                .map(room -> {
                    //  대상 유저 정보 가져오기
                    String targetUsername = room.getUser1().equals(username) ? room.getUser2() : room.getUser1();
                    UserEntity targetUser = userRepository.findByUsername(targetUsername).orElse(null);

                    ChatMessageEntity lastMessage = chatMessageRepository
                            .findTopByChatRoom_RoomIdOrderBySentAtDesc(room.getRoomId());
                    Long unreadCount = chatMessageRepository
                            .countByChatRoom_RoomIdAndSenderNotAndIsReadFalse(room.getRoomId(), username);

                    return ChatRoomResponseDto.builder()
                            .roomId(room.getRoomId())
                            .user1(room.getUser1())
                            .user2(room.getUser2())
                            .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                            .lastMessageTime(lastMessage != null ? lastMessage.getSentAt() : null)
                            .unreadCount(unreadCount)
                            //  대상 유저 정보 추가
                            .targetName(targetUser != null ? targetUser.getName() : "알 수 없음")
                            .targetUsername(targetUsername)
                            .targetImg(targetUser != null ? targetUser.getUserImg() : null)
                            .build();
                })
                .sorted((a, b) -> {
                    if (a.getLastMessageTime() == null) return 1;
                    if (b.getLastMessageTime() == null) return -1;
                    return b.getLastMessageTime().compareTo(a.getLastMessageTime());
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomEntity createOrGetChatRoom(String user1, String user2) {
        return createOrGetChatRoom(user1, user2, false);
    }

    @Override
    public ChatRoomEntity createOrGetChatRoom(String user1, String user2, boolean isSuperChat) {
        return chatRoomRepository.findByUser1AndUser2(user1, user2)
                .orElseGet(() -> {
                    ChatRoomEntity newRoom = new ChatRoomEntity(user1, user2);
                    chatRoomRepository.save(newRoom);

                    String message = isSuperChat
                            ? "💎 " + user1 + "님이 슈퍼챗을 사용하였습니다!"
                            : "💬 " + user1 + "님과 채팅방이 생성되었습니다.";

                    ChatMessageEntity systemMessage = ChatMessageEntity.builder()
                            .chatRoom(newRoom)
                            .sender("SYSTEM")
                            .content(message)
                            .sentAt(LocalDateTime.now())
                            .isRead(true)
                            .build();

                    chatMessageRepository.save(systemMessage);

                    return newRoom;
                });
    }

    @Override
    public void leaveChatRoom(Long roomId, String username) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        boolean changed = false;

        if (username.equals(room.getUser1())) {
            room.setUser1(null);
            changed = true;
        } else if (username.equals(room.getUser2())) {
            room.setUser2(null);
            changed = true;
        }

        if (changed) {
            chatRoomRepository.save(room);
        }
    }
}
