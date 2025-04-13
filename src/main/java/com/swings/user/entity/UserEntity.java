package com.swings.user.entity;

import com.swings.email.entity.UserVerifyEntity;
import com.swings.feed.entity.CommentEntity;
import com.swings.feed.entity.FeedEntity;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.social.entity.SocialEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
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

    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일

    @Column(nullable = false, length = 15)
    private String phonenumber; // 전화번호

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일


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

    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String userImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 역할 (ENUM)

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityRegion activityRegion; // 활동 지역 (도/광역시 단위 ENUM)

    //포인트 금액
    @Column(nullable = false)
    private int pointBalance = 0;

    //회원 인증 토큰 발급 여부
    @Column(nullable = false)
    private boolean isVerified = false;
    

    //Cascade 관리
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPointEntity> pointHistory;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SocialEntity> followingList;

    @OneToMany(mappedBy = "followee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SocialEntity> followerList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserVerifyEntity> userVerifyList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FeedEntity> feeds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MatchParticipantEntity> matchParticipations;

    @OneToMany(mappedBy = "host", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MatchGroupEntity> createdGroups;



    // ✅ `createdAt`이 NULL이면 자동 설정 (JPA에서 NULL 방지)
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
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

    public enum ActivityRegion {
        SEOUL, BUSAN, DAEGU, INCHEON, GWANGJU,
        DAEJEON, ULSAN, SEJONG,
        GYEONGGI, GANGWON, CHUNGBUK, CHUNGNAM,
        JEONBUK, JEONNAM, GYEONGBUK, GYEONGNAM,
        JEJU;

        public static ActivityRegion fromString(String value) {
            return Stream.of(ActivityRegion.values())
                    .filter(e -> e.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ActivityRegion: " + value));
        }
    }

    // 🎯 한국식 나이 계산 메서드
    public int getKoreanAge() {
        int currentYear = LocalDate.now().getYear();
        int birthYear = this.birthDate.getYear();
        return currentYear - birthYear + 1;
    }
}
