package com.swings.chat.service;

import com.swings.chat.entity.ChatMessageEntity;
import com.swings.chat.entity.ChatRoomEntity;
import com.swings.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CHAT_KEY_PREFIX = "chat_room_";

    // 메시지 저장
    @Transactional
    public void saveMessage(Long roomId, String sender, String content) {
        ChatMessageEntity chatMessage = ChatMessageEntity.builder()
                .chatRoom(ChatRoomEntity.builder().roomId(roomId).build()) // ✅ chatRoomId가 아닌 chatRoom 객체 사용
                .sender(sender)
                .content(content)
                .build();

        // DB에 저장
        chatMessageRepository.save(chatMessage);

        // Redis에 저장 (최근 50개 유지)
        String redisKey = CHAT_KEY_PREFIX + roomId;
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.rightPush(redisKey, sender + ": " + content);
        if (listOps.size(redisKey) > 50) {
            listOps.leftPop(redisKey);
        }
    }

    // 최근 메시지 가져오기
    public List<String> getRecentMessages(Long roomId) {
        String redisKey = CHAT_KEY_PREFIX + roomId;
        return redisTemplate.opsForList().range(redisKey, 0, -1);
    }

    public List<ChatMessageEntity> getMessagesByRoomId(Long roomId) {
        // ✅ 해당 메서드를 사용하려면 리포지토리에 정확히 선언되어 있어야 함
        return chatMessageRepository.findByChatRoom_RoomIdOrderBySentAtAsc(roomId);
    }


}
