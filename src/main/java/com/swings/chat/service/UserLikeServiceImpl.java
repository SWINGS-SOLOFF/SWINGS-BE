package com.swings.chat.service;

import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.entity.UserLikeEntity;
import com.swings.chat.repository.UserLikeRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLikeServiceImpl implements UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final ChatRoomService chatRoomService;
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

    @Override
    public List<UserLikeEntity> getLikesReceived(String toUserId) {
        return userLikeRepository.findByToUserId(toUserId);
    }

    @Override
    public Map<String, List<SentLikeDTO>> getSentAndReceivedLikes(String userId) {
        // 내가 보낸 좋아요
        List<UserLikeEntity> sentLikes = userLikeRepository.findByFromUserId(userId);

        // 내가 받은 좋아요
        List<UserLikeEntity> receivedLikes = userLikeRepository.findByToUserId(userId);

        // 보낸 리스트 → SentLikeDTO로 변환
        List<SentLikeDTO> sentResult = sentLikes.stream().map(like -> {
            UserEntity toUser = userRepository.findByUsername(like.getToUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            boolean isMutual = userLikeRepository.existsByFromUserIdAndToUserId(like.getToUserId(), userId);

            return SentLikeDTO.builder()
                    .username(toUser.getUsername())
                    .name(toUser.getName())
                    .userImg(toUser.getUserImg())
                    .isMutual(isMutual)
                    .build();
        }).collect(Collectors.toList());

        // 받은 리스트 → fromUserId 기준으로 유저 정보 조회해서 SentLikeDTO 변환
        List<SentLikeDTO> receivedResult = receivedLikes.stream().map(like -> {
            UserEntity fromUser = userRepository.findByUsername(like.getFromUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            boolean isMutual = userLikeRepository.existsByFromUserIdAndToUserId(fromUser.getUsername(), userId);

            return SentLikeDTO.builder()
                    .username(fromUser.getUsername())
                    .name(fromUser.getName())
                    .userImg(fromUser.getUserImg())
                    .isMutual(isMutual)
                    .build();
        }).collect(Collectors.toList());

        // Map으로 감싸서 반환
        Map<String, List<SentLikeDTO>> result = new HashMap<>();
        result.put("sentLikes", sentResult);
        result.put("receivedLikes", receivedResult);
        return result;
    }
}
