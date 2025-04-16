package com.swings.feed.dto;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.entity.FeedEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 게시물 정보 프론트엔드로 전달하는 DTO 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDTO {
    private Long feedId;               // 피드 ID
    private Long userId;               // 작성자 ID
    private String username;           // 작성자 이름
    private String imageUrl;           // 이미지 URL
    private String caption;            // 게시물 내용
    private LocalDateTime createdAt;   // 생성 시간
    private int likes;                 // 좋아요 수
    private boolean liked;             // 로그인 유저의 좋아요 여부
    private String userProfilePic;     // 작성자 프로필 이미지
    private List<CommentDTO> comments = new ArrayList<>(); // 댓글 목록

    // FeedEntity 객체를 받아서 FeedDTO로 변환하는 생성자
    public FeedDTO(FeedEntity feedEntity, boolean liked) {
        this.feedId = feedEntity.getFeedId();
        this.userId = feedEntity.getUser().getUserId();
        this.username = feedEntity.getUser().getUsername();
        this.userProfilePic = feedEntity.getUser().getUserImg();
        this.imageUrl = feedEntity.getImageUrl();
        this.caption = feedEntity.getCaption();
        this.createdAt = feedEntity.getCreatedAt();
        this.likes = feedEntity.getLikes();
        this.liked = liked;

        // 댓글은 CommentEntity -> CommentDTO로 변환
        this.comments = feedEntity.getComments().stream()
                .map(comment -> new CommentDTO(comment))
                .collect(Collectors.toList());
    }

    // FeedEntity만 받는 생성자 추가 (기본적으로 liked를 false로 설정)
    public FeedDTO(FeedEntity feedEntity) {
        this(feedEntity, false);
    }

}