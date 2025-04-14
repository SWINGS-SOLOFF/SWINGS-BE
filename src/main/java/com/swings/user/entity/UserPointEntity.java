package com.swings.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_points")
public class UserPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    private int amount; // +충전 / -사용

    @Enumerated(EnumType.STRING)
    private PointType type; // 충전, 사용, 취소, 관리자 임의 충전(etc 초대)

    private String description; //사용 상세 내역

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum PointType {
        CHARGE, USE, CANCEL, ADJUST
    }
}