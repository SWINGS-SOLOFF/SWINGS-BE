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
@Table(name = "userLikes") // DB 테이블 이름
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

    //  예약어 문제 방지 → 컬럼명에 백틱(`) 적용
    @Column(name = "`match`", nullable = false)
    private boolean match;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
