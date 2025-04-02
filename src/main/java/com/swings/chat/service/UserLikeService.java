package com.swings.chat.service;

import com.swings.chat.entity.UserLikeEntity;
import com.swings.chat.repository.UserLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final ChatRoomService chatRoomService; // ✅ 채팅방 서비스 주입

    // 좋아요 저장
    @Transactional
    public void likeUser(String fromUserId, String toUserId) {
        boolean alreadyLiked = userLikeRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);

        if (!alreadyLiked) {
            userLikeRepository.save(UserLikeEntity.builder()
                    .fromUserId(fromUserId)
                    .toUserId(toUserId)
                    .build());
        }

        // ✅ 서로 좋아요를 눌렀다면 → 채팅방 생성
        boolean matched = isMatched(fromUserId, toUserId);
        if (matched) {
            chatRoomService.createOrGetChatRoom(fromUserId, toUserId);
        }
    }

    public boolean isMatched(String fromUserId, String toUserId) {
        return userLikeRepository.countMutualLike(fromUserId, toUserId) == 2;
    }
}
