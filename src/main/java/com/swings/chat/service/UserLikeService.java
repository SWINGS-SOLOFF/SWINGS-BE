package com.swings.chat.service;

import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.entity.UserLikeEntity;
import com.swings.chat.repository.UserLikeRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final ChatRoomService chatRoomService; // ✅ 채팅방 서비스 주입
    private final UserRepository userRepository;

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

    public List<SentLikeDTO> getSentLikesWithMutual(String fromUsername) {
        List<UserLikeEntity> sentLikes = userLikeRepository.findByFromUserId(fromUsername);

        return sentLikes.stream().map(like -> {
            String toUserId = like.getToUserId();

            // 유저 정보 불러오기
            UserEntity toUser = userRepository.findByUsername(toUserId)
                    .orElseThrow(() -> new IllegalArgumentException("상대방 유저 없음: " + toUserId));

            // 쌍방 여부 판단
            boolean isMutual = userLikeRepository.existsByFromUserIdAndToUserId(
                    toUserId, fromUsername // 반대 방향 좋아요 확인
            );

            return SentLikeDTO.builder()
                    .username(toUser.getUsername())
                    .name(toUser.getName())
                    .userImg(toUser.getUserImg())
                    .isMutual(isMutual)
                    .build();
        }).collect(Collectors.toList());
    }


}
