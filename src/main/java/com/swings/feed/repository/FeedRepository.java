package com.swings.feed.repository;

import com.swings.feed.entity.FeedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    int countByUser_UserId(Long userId);
    List<FeedEntity> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    List<FeedEntity> findByUser_UserId(Long userId);
    Page<FeedEntity> findByUser_UserIdIn(List<Long> userIds, Pageable pageable);
}