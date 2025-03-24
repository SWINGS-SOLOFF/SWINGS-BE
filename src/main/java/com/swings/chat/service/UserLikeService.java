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

    // 좋아요 저장
    @Transactional
    public void likeUser(String fromUserId, String toUserId) {
        // fromUserId가 이미 좋아요를 눌렀는지 확인
        if (userLikeRepository.findByFromUserId(fromUserId).isEmpty()) {
            userLikeRepository.save(UserLikeEntity.builder()
                    .fromUserId(fromUserId)
                    .toUserId(toUserId)
                    .build());
        }
    }

    // 두 유저가 서로 좋아요 했는지 확인 (매칭 여부)
    public boolean isMatched(String fromUserId, String toUserId) {
        return userLikeRepository.countMutualLike(fromUserId, toUserId) == 2;
    }
}
