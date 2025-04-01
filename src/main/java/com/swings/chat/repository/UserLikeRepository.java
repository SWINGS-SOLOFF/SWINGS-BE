package com.swings.chat.repository;

import com.swings.chat.entity.UserLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Long> {

    // 특정 유저가 좋아요를 누른 기록 조회
    List<UserLikeEntity> findByFromUserId(String fromUserId);

    // 특정 유저가 받은 좋아요 조회
    List<UserLikeEntity> findByToUserId(String toUserId);

    // 두 유저가 서로 좋아요 했는지 확인 (매칭 여부)
    @Query("SELECT COUNT(u) FROM UserLikeEntity u WHERE " +
            "(u.fromUserId = :fromUserId AND u.toUserId = :toUserId) OR " +
            "(u.fromUserId = :toUserId AND u.toUserId = :fromUserId)")
    int countMutualLike(@Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId);
}
