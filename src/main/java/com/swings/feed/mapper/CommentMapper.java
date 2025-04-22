package com.swings.feed.mapper;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.entity.CommentEntity;

public class CommentMapper {
	
    public static CommentDTO toDTO(CommentEntity entity) {
        if (entity == null) return null;

        return CommentDTO.builder()
                .commentId(entity.getCommentId())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .username(entity.getUser() != null ? entity.getUser().getUsername() : "Unknown")
                .userProfilePic(entity.getUser() != null ? entity.getUser().getUserImg() : null)
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    
}
