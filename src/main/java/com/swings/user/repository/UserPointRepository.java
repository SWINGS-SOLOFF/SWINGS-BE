package com.swings.user.repository;

import com.swings.user.entity.UserEntity;
import com.swings.user.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserPointRepository extends JpaRepository<UserPointEntity, Long> {

    // 특정 유저의 포인트 내역을 최신순으로 조회
    @Query("SELECT p FROM UserPointEntity p WHERE p.user.username = :username ORDER BY p.createdAt DESC")
    List<UserPointEntity> findPointLogByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findByUsername(@Param("username") String username);
}