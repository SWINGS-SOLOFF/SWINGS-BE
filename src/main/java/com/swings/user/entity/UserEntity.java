package com.swings.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.stream.Stream;

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
    private Long user_id; // 자동 증가하는 기본 키

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
    private String phone_number; // 전화번호

    @Column(nullable = false, length = 50)
    private String job; // 직업

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GolfSkill golf_skill; // 골프 실력 (ENUM)

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

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String user_img;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 역할 (ENUM)

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created_at;

    // ✅ `createdAt`이 NULL이면 자동 설정 (JPA에서 NULL 방지)
    @PrePersist
    protected void onCreate() {
        if (created_at == null) {
            created_at = new Timestamp(System.currentTimeMillis());
        }
    }



    // 🔹 Enum 변환 메서드 추가
    public enum GolfSkill {
        beginner, intermediate, advanced;

        public static GolfSkill fromString(String value) {
            return Stream.of(GolfSkill.values())
                    .filter(e -> e.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid GolfSkill: " + value));
        }
    }

    public enum YesNo {
        yes, no;

        public static YesNo fromString(String value) {
            return Stream.of(YesNo.values())
                    .filter(e -> e.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid YesNo value: " + value));
        }
    }

    public enum Role {
        player, admin;

        public static Role fromString(String value) {
            return Stream.of(Role.values())
                    .filter(e -> e.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Role: " + value));
        }
    }

    public enum Gender {
        male, female;

        public static Gender fromString(String value) {
            return Stream.of(Gender.values())
                    .filter(e -> e.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Gender: " + value));
        }
    }
}
