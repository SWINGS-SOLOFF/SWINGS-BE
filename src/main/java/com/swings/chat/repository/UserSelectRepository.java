package com.swings.chat.repository;

import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSelectRepository extends JpaRepository<UserEntity, Long> {

    //  로그인 유저 정보
    Optional<UserEntity> findByUsername(String username);

    //  좋아요 누른 유저 리스트
    @Query("SELECT ul.toUserId FROM UserLikeEntity ul WHERE ul.fromUserId = :fromUserId")
    List<String> findLikedUsernames(@Param("fromUserId") String fromUserId);

    //  싫어요 누른 유저 리스트
    @Query("SELECT ud.toUsername FROM UserDislikeEntity ud WHERE ud.fromUsername = :fromUsername")
    List<String> findDislikedUsernames(@Param("fromUsername") String fromUsername);

    //  채팅방 상대 유저 리스트
    @Query("SELECT CASE " +
            "WHEN cr.user1 = :username THEN cr.user2 " +
            "ELSE cr.user1 END " +
            "FROM ChatRoomEntity cr " +
            "WHERE cr.user1 = :username OR cr.user2 = :username")
    List<String> findChatUsernames(@Param("username") String username);

    // 제외 리스트 기반 필터링된 유저 중 1명 랜덤 추천 (LIMIT 1 사용 → 에러 방지)
    @Query(
            value = "SELECT * FROM users " +
                    "WHERE gender <> :gender " +
                    "AND username NOT IN (:excludedUsernames) " +
                    "ORDER BY RAND() " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<UserEntity> findFilteredRandomUser(@Param("gender") String gender,
                                                @Param("excludedUsernames") List<String> excludedUsernames);

    //  fallback: 필터 없이 성별만 다르면 추천
    @Query(
            value = "SELECT * FROM users " +
                    "WHERE gender <> :gender " +
                    "ORDER BY RAND() " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<UserEntity> findRandomUser(@Param("gender") String gender);
}
