package com.swings.chat.repository;

import com.swings.chat.entity.UserLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Long> {

    // 내가 특정 유저를 좋아했는지 여부 (좋아요 중복 방지용 등)
    boolean existsByFromUserIdAndToUserId(String fromUserId, String toUserId);

    // 특정 유저가 좋아요를 누른 기록 (보낸 좋아요)
    List<UserLikeEntity> findByFromUserId(String fromUserId);

    // 내가 받은 좋아요 리스트 (toUserId 기준)
    List<UserLikeEntity> findByToUserId(String toUserId);

    int countByFromUserIdAndCreatedAtAfter(String fromUserId, LocalDateTime createdAt);

    // 두 유저가 서로 좋아요 했는지 확인 (쌍방 여부 체크)
    @Query("SELECT COUNT(u) FROM UserLikeEntity u WHERE " +
            "(u.fromUserId = :fromUserId AND u.toUserId = :toUserId) OR " +
            "(u.fromUserId = :toUserId AND u.toUserId = :fromUserId)")
    int countMutualLike(@Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId);
}
