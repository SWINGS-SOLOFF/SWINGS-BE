package com.swings.chat.service;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.repository.ChatMessageRepository;
import com.swings.chat.repository.ChatRoomRepository;
import com.swings.notification.service.FCMService;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final FCMService fcmService;
    private final UserRepository userRepository;

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

    // ✅ 기본 저장 로직 (FCM 포함, 내부 재사용 가능)
    @Override
    public ChatMessageEntity saveMessage(Long roomId, String sender, String content) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessageEntity message = ChatMessageEntity.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();

        ChatMessageEntity savedMessage = chatMessageRepository.save(message);

        // 📲 푸시알림 전송
        String receiverUsername = chatRoom.getUser1().equals(sender)
                ? chatRoom.getUser2()
                : chatRoom.getUser1();

        UserEntity receiver = userRepository.findByUsername(receiverUsername).orElse(null);

        if (receiver != null && receiver.getPushToken() != null) {
            String preview = content.length() > 20 ? content.substring(0, 20) + "..." : content;
            fcmService.sendPush(
                    receiver.getPushToken(),
                    "💬 새 메시지",
                    sender + ": " + preview
            );
        }

        return savedMessage;
    }

    // ✅ WebSocket 전용: 저장 + senderName 포함된 DTO 반환
    @Override
    public ChatMessageDTO saveAndReturnDTO(Long roomId, String sender, String content) {
        ChatMessageEntity saved = saveMessage(roomId, sender, content);

        UserEntity senderUser = userRepository.findByUsername(sender)
                .orElse(null);

        return ChatMessageDTO.builder()
                .roomId(saved.getChatRoom().getRoomId())
                .sender(saved.getSender())
                .senderName(senderUser != null ? senderUser.getName() : saved.getSender())
                .content(saved.getContent())
                .sentAt(saved.getSentAt())
                .build();
    }

    // ✅ 전체 메시지를 DTO 리스트로 변환해서 반환
    @Override
    public List<ChatMessageDTO> getMessageDTOsByRoomId(Long roomId) {
        List<ChatMessageEntity> messages = chatMessageRepository.findByChatRoom_RoomIdOrderBySentAtAsc(roomId);

        return messages.stream().map(msg -> {
            UserEntity senderUser = userRepository.findByUsername(msg.getSender())
                    .orElse(null);

            return ChatMessageDTO.builder()
                    .roomId(msg.getChatRoom().getRoomId())
                    .sender(msg.getSender())
                    .senderName(senderUser != null ? senderUser.getName() : msg.getSender())
                    .content(msg.getContent())
                    .sentAt(msg.getSentAt())
                    .build();
        }).toList();
    }
}
