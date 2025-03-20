package com.swings.feed.repository;

import com.swings.feed.entity.FeedEntity;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
}