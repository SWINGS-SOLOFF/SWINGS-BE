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
@Table(name = "users") // DB 테이블 이름과 매핑
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 자동 증가하는 기본 키

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 사용자 아이디

    @Column(nullable = false, length = 255)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false, length = 50)
    private String name; // 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // 성별 (ENUM)

    @Column(nullable = false, length = 15)
    private String phonenumber; // 전화번호

    @Column(nullable = false, length = 50)
    private String job; // 직업

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GolfSkill golfSkill; // 골프 실력 (ENUM)

    @Column(nullable = false, length = 10)
    private String mbti; // MBTI

    @Column(nullable = false, columnDefinition = "TEXT")
    private String hobbies; // 취미

    @Column(nullable = false, length = 50)
    private String religion; // 종교

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo smoking; // 흡연 여부 (ENUM)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YesNo drinking; // 음주 여부 (ENUM)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduce; // 자기소개


    @Column(nullable = false, length = 255)
    private String userImg; // 프로필 사진 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 역할 (ENUM)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일 (자동 설정)

    public enum GolfSkill {
        beginner, intermediate, advanced
    }

    public enum YesNo {
        yes, no
    }

    public enum Role {
        player, admin
    }


    public enum Gender {
        male, female
    }
}
