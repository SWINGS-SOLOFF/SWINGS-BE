package com.swings.chat.service;

import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.entity.UserLikeEntity;
import com.swings.chat.repository.UserLikeRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import com.swings.user.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final UserPointService userPointService; // 💰 추가됨

    @Override
    @Transactional
    public void likeUser(String fromUserId, String toUserId) {
        boolean alreadyLiked = userLikeRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
        if (alreadyLiked) return; // 이미 눌렀으면 패스

        // ✅ 무료 좋아요 여부 확인
        boolean canSendFreeLike = canSendLike(fromUserId);

        // ✅ 무료 횟수 끝났으면 유료 차감 시도
        if (!canSendFreeLike) {
            // 🪙 포인트 1코인 차감 시도
            userPointService.usePoint(fromUserId, 1, "좋아요 사용");
        }

        // ✅ 좋아요 저장
        userLikeRepository.save(UserLikeEntity.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .build());

        // ✅ 쌍방이면 채팅방 생성
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
        List<UserLikeEntity> sentLikes = userLikeRepository.findByFromUserId(userId);
        List<UserLikeEntity> receivedLikes = userLikeRepository.findByToUserId(userId);

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

        List<SentLikeDTO> receivedResult = receivedLikes.stream().map(like -> {
            UserEntity fromUser = userRepository.findByUsername(like.getFromUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            boolean isMutual = userLikeRepository.existsByFromUserIdAndToUserId(userId, fromUser.getUsername());
            return SentLikeDTO.builder()
                    .username(fromUser.getUsername())
                    .name(fromUser.getName())
                    .userImg(fromUser.getUserImg())
                    .isMutual(isMutual)
                    .build();
        }).collect(Collectors.toList());

        Map<String, List<SentLikeDTO>> result = new HashMap<>();
        result.put("sentLikes", sentResult);
        result.put("receivedLikes", receivedResult);
        return result;
    }

    @Override
    public boolean canSendLike(String username) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        int count = userLikeRepository.countByFromUserIdAndCreatedAtAfter(username, todayStart);
        return count < 3;
    }

    @Override
    public int countTodayLikes(String username, LocalDateTime since) {
        return userLikeRepository.countByFromUserIdAndCreatedAtAfter(username, since);
    }



}
