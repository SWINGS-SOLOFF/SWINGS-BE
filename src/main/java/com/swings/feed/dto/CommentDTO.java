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

    public CommentDTO(CommentEntity commentEntity) {
        this.commentId = commentEntity.getCommentId();
        this.content = commentEntity.getContent();
        this.createdAt = commentEntity.getCreatedAt();

        if (commentEntity.getUser() != null) {
            this.userId = commentEntity.getUser().getUserId();
            this.username = commentEntity.getUser().getUsername();
            this.userProfilePic = commentEntity.getUser().getUserImg();
        } else {
            this.userId = null;
            this.username = "Unknown";
            this.userProfilePic = null;
        }
    }
}
