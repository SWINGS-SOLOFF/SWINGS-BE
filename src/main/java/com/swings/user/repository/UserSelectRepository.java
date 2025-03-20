package com.swings.user.repository;

import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSelectRepository extends JpaRepository<UserEntity, Long> {

    // 현재 사용자와 성별이 다른 무작위 추천 유저 조회
    @Query("SELECT u FROM UserEntity u WHERE u.gender <> :gender ORDER BY RAND() LIMIT 1")
    Optional<UserEntity> findRandomUser(@Param("gender") String gender);

    // 같은 성별이 아닌 유저 중에서 특정 유저(excludedUserId)를 제외하고 추천하는 쿼리
    @Query("SELECT u FROM UserEntity u WHERE u.gender <> :gender AND u.username <> :excludedUsername ORDER BY RAND() LIMIT 1")
    Optional<UserEntity> findNextRandomUser(@Param("gender") String gender, @Param("excludedUsername") String excludedUsername);

    // username 기반으로 조회하는 메서드 추가
    Optional<UserEntity> findByUsername(String username);
}
