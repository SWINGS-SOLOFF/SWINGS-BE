package com.swings.email.repository;

import com.swings.email.entity.UserVerifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerifyRepository extends JpaRepository<UserVerifyEntity, Long> {

    // 토큰으로 조회
    Optional<UserVerifyEntity> findByToken(String token);
}
