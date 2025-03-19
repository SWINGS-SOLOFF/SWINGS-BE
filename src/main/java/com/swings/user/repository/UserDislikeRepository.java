package com.swings.user.repository;

import com.swings.user.entity.UserDislikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDislikeRepository extends JpaRepository<UserDislikeEntity, Long> {

    Optional<UserDislikeEntity> findByFromUsernameAndToUsername(String fromUsername, String toUsername);
}