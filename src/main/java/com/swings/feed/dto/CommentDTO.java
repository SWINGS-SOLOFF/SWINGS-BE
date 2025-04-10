package com.swings.feed.dto;

import com.swings.feed.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long commentId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private String userProfilePic;

    // CommentEntity 객체를 받아서 CommentDTO로 변환하는 생성자
    public CommentDTO(CommentEntity commentEntity) {
        this.commentId = commentEntity.getCommentId();
        this.userId = commentEntity.getUser().getUserId();
        this.content = commentEntity.getContent();
        this.createdAt = commentEntity.getCreatedAt();
        this.userProfilePic = commentEntity.getUser().getUserImg();
    }
}
