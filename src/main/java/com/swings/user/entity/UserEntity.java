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

    @Column(nullable = false, columnDefinition = "TEXT") // Base64 이미지 저장 가능
    private String userImg; // 프로필 사진 (Base64)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 역할 (ENUM)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일 (자동 설정)

    // 🔹 Enum 변환 메서드 추가 (프론트에서 보낸 String을 Enum으로 변환)
    public enum GolfSkill {
        beginner, intermediate, advanced;

        public static GolfSkill fromString(String value) {
            return GolfSkill.valueOf(value.toLowerCase());
        }
    }

    public enum YesNo {
        yes, no;

        public static YesNo fromString(String value) {
            return YesNo.valueOf(value.toLowerCase());
        }
    }

    public enum Role {
        player, admin;

        public static Role fromString(String value) {
            return Role.valueOf(value.toLowerCase());
        }
    }

    public enum Gender {
        male, female;

        public static Gender fromString(String value) {
            return Gender.valueOf(value.toLowerCase());
        }
    }
}
