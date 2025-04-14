package com.swings.chat.service;

import com.swings.chat.dto.SentLikeDTO;

import java.util.List;

public interface UserLikeService {

    // 좋아요 요청 처리
    void likeUser(String fromUserId, String toUserId);

    // 쌍방 좋아요 여부 확인
    boolean isMatched(String fromUserId, String toUserId);

    // 내가 보낸 좋아요 리스트 + 쌍방 여부 확인 포함
    List<SentLikeDTO> getSentLikesWithMutual(String fromUsername);
}