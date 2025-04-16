package com.swings.chat.service;

import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.entity.UserLikeEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UserLikeService {

    // 좋아요 요청 처리
    void likeUser(String fromUserId, String toUserId);

    // 쌍방 좋아요 여부 확인
    boolean isMatched(String fromUserId, String toUserId);

    // 내가 보낸 좋아요 리스트 + 쌍방 여부 확인 포함
    List<SentLikeDTO> getSentLikesWithMutual(String fromUsername);

    // 받은 좋아요 엔티티
    List<UserLikeEntity> getLikesReceived(String toUserId);

    // 무료 좋아요 가능 여부 (3회 이하인지 확인)
    boolean canSendLike(String username);

    int countTodayLikes(String username, LocalDateTime since);

    // 🔥 보낸 + 받은 좋아요 DTO로 통합 반환
    Map<String, List<SentLikeDTO>> getSentAndReceivedLikes(String userId);
}
