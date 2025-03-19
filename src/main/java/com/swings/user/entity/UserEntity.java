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

    @Column(nullable = false, length = 15)
    private String phonenumber; // 전화번호

    @Column(nullable = false, length = 50)
    private String job; // 직업

    @Column(nullable = false, length = 10)
    private String gender; // 성별 ("MALE", "FEMALE")

    @Column(nullable = false, length = 10)
    private String mbti; // MBTI

    @Column(nullable = false, columnDefinition = "TEXT")
    private String hobbies; // 취미

    @Column(nullable = false, length = 50)
    private String religion; // 종교

    @Column(nullable = false, length = 255)
    private String userImg; // 프로필 사진 URL

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일 (자동 설정)


}
