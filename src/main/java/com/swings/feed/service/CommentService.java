package com.swings.feed.service;

import com.swings.feed.entity.CommentEntity;
import java.util.List;

public interface CommentService {
    CommentEntity addComment(Long feedId, Long userId, String content);
    void deleteComment(Long commentId);
    List<CommentEntity> getCommentsByFeedId(Long feedId);
    CommentEntity updateComment(Long commentId, String content);
}
