package com.swings.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "userLikes") // DB 테이블과 매핑
public class UserLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId; // 좋아요 ID (자동 증가)

    @Column(nullable = false, length = 50)
    private String fromUserId; // 좋아요를 누른 사용자 ID

    @Column(nullable = false, length = 50)
    private String toUserId; // 좋아요를 받은 사용자 ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 좋아요 누른 시간 (자동 설정)
}
