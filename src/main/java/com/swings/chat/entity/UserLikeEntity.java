package com.swings.chat.entity;

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
    private Long likeId;

    @Column(nullable = false, length = 50)
    private String fromUserId;

    @Column(nullable = false, length = 50)
    private String toUserId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ 저장 전에 자동으로 현재 시간 세팅
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
