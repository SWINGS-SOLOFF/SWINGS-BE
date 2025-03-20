package com.swings.feed.dto;

import com.swings.feed.dto.CommentDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDTO {
    private Long feedId;
    private Long userId;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
    private int likes;
    private List<CommentDTO> comments = new ArrayList<>();
}
