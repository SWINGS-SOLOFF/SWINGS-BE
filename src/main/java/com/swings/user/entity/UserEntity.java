package com.swings.user.entity;

import com.swings.user.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GolfSkill golfSkill;

    @Column(nullable = false)
    private String mbti;

    @Column(nullable = false)
    private String hobbies;

    @Column(nullable = false)
    private String religion;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Smoking smoking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Drinking drinking;

    @Column(nullable = false, length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String introduce; //유저 자기소개

    private String userImg; // 해당 유저 사진 업로드

}
