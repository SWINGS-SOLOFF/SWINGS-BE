package com.swings.social.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "social")
public class SocialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로워
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    // 팔로우 대상
    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
