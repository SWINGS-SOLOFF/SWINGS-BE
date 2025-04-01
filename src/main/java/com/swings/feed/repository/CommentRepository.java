package com.swings.feed.repository;

import com.swings.feed.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // feed 엔티티의 feedId를 참조하기 위해 '_'를 사용
    List<CommentEntity> findByFeed_feedIdOrderByCreatedAtDesc(Long feedId);

    List<CommentEntity> findByFeed_FeedId(Long feedFeedId);
}
