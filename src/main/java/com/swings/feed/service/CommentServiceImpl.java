package com.swings.feed.service;

import com.swings.feed.entity.CommentEntity;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.CommentRepository;
import com.swings.feed.repository.FeedRepository;
import com.swings.feed.service.CommentService;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, FeedRepository feedRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    @Override
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

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        FeedEntity feed = comment.getFeed();
        feed.getComments().remove(comment); 

        commentRepository.delete(comment); 
    }

    @Override
    public List<CommentEntity> getCommentsByFeedId(Long feedId) {
        if (feedId == null) {
            throw new IllegalArgumentException("Feed ID must not be null");
        }

        List<CommentEntity> comments = commentRepository.findByFeed_FeedId(feedId);

        return comments != null ? comments : List.of();
    }
}
