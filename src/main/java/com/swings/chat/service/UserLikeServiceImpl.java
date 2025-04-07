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
public class UserLikeServiceImpl implements UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final ChatRoomService chatRoomService; // ✅ 채팅방 서비스 사용
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void likeUser(String fromUserId, String toUserId) {
        boolean alreadyLiked = userLikeRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);

        if (!alreadyLiked) {
            userLikeRepository.save(UserLikeEntity.builder()
                    .fromUserId(fromUserId)
                    .toUserId(toUserId)
                    .build());
        }

        // 쌍방 좋아요면 채팅방 생성
        if (isMatched(fromUserId, toUserId)) {
            chatRoomService.createOrGetChatRoom(fromUserId, toUserId);
        }
    }

    @Override
    public boolean isMatched(String fromUserId, String toUserId) {
        return userLikeRepository.countMutualLike(fromUserId, toUserId) == 2;
    }

    @Override
    public List<SentLikeDTO> getSentLikesWithMutual(String fromUsername) {
        List<UserLikeEntity> sentLikes = userLikeRepository.findByFromUserId(fromUsername);

        return sentLikes.stream().map(like -> {
            String toUserId = like.getToUserId();

            UserEntity toUser = userRepository.findByUsername(toUserId)
                    .orElseThrow(() -> new IllegalArgumentException("상대방 유저 없음: " + toUserId));

            boolean isMutual = userLikeRepository.existsByFromUserIdAndToUserId(toUserId, fromUsername);

            return SentLikeDTO.builder()
                    .username(toUser.getUsername())
                    .name(toUser.getName())
                    .userImg(toUser.getUserImg())
                    .isMutual(isMutual)
                    .build();
        }).collect(Collectors.toList());
    }
}
