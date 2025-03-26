package com.swings.feed.repository;

import com.swings.feed.entity.FeedEntity;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    int countByUser_UserId(Long userId);
    List<FeedEntity> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    List<FeedEntity> findByUser_UserId(Long userId);

}