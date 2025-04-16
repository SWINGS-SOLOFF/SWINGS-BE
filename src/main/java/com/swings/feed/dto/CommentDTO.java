package com.swings.feed.dto;

import com.swings.feed.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

// 댓글 정보를 클라이언트에 전달 용도 DTO 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long commentId;           // 댓글 ID
    private Long userId;              // 댓글 작성자 ID
    private String username;          // 댓글 작성자 이름
    private String content;           // 댓글 내용
    private LocalDateTime createdAt;  // 댓글 작성 시각
    private String userProfilePic;    // 작성자 프로필 이미지 URL

    // CommentEntity -> CommentDTO 변환
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
