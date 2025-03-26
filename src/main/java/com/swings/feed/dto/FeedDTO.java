package com.swings.feed.dto;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.entity.FeedEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDTO {
    private Long feedId;
    private Long userId;
    private String username;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
    private int likes;
    private List<CommentDTO> comments = new ArrayList<>();

    // FeedEntity 객체를 받아서 FeedDTO로 변환하는 생성자
    public FeedDTO(FeedEntity feedEntity) {
        this.feedId = feedEntity.getFeedId();
        this.userId = feedEntity.getUser().getUserId();
        this.imageUrl = feedEntity.getImageUrl();
        this.caption = feedEntity.getCaption();
        this.createdAt = feedEntity.getCreatedAt();
        this.likes = feedEntity.getLikes();

        // 댓글은 CommentEntity -> CommentDTO로 변환
        this.comments = feedEntity.getComments().stream()
                .map(comment -> new CommentDTO(comment))
                .collect(Collectors.toList());
    }

}
