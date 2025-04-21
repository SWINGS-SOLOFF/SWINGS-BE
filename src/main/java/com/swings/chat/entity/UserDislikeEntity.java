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
@Table(name = "userDislikes") // 싫어요 테이블
public class UserDislikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislikeId; // 싫어요 ID (자동 증가)

    @Column(nullable = false, length = 50)
    private String fromUsername; // 싫어요를 누른 사용자

    @Column(nullable = false, length = 50)
    private String toUsername; // 싫어요를 받은 사용자

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 싫어요 누른 시간

    //  해결: 자동으로 현재 시간 저장
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}