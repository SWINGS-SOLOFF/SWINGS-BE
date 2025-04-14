package com.swings.email.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_verify")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVerifyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userVerifyId;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerifyType type; // EMAIL_VERIFY, PASSWORD_RESET 등 구분

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    public enum VerifyType {
        EMAIL_VERIFY,
        PASSWORD_RESET
    }
}