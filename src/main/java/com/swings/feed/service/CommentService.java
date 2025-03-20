package com.swings.feed.service;

import com.swings.feed.entity.CommentEntity;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.CommentRepository;
import com.swings.feed.repository.FeedRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, FeedRepository feedRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    // 댓글 추가
    public CommentEntity addComment(Long feedId, Long userId, String content) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        CommentEntity comment = CommentEntity.builder()
                .feed(feed)
                .user(user)
                .content(content)
                .build();

        return commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 피드에 대한 댓글 조회
    public List<CommentEntity> getCommentsByFeedId(Long feedId) {
        return commentRepository.findByFeed_FeedIdOrderByCreatedAtDesc(feedId);
    }
}