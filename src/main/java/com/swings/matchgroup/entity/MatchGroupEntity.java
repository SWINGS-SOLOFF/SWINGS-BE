package com.swings.matchgroup.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matchGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private UserEntity host; // 방장 (그룹 생성자)

    @Column(nullable = true)
    private String groupName; // 그룹 이름

    @Column(nullable = false)
    private String location; // 골프장 위치

    private Double latitude; // 위도
    private Double longitude; // 경도

    @Column(nullable = false)
    private String schedule; // 일정 (날짜, 시간)

    @Column(nullable = false)
    private String playStyle; // 플레이 스타일 (ex. 유쾌한, 진지한 등)

    @Column(nullable = false)
    private int femaleLimit; // 여성 최대 인원

    @Column(nullable = false)
    private int maleLimit; // 남성 최대 인원

    @Column(nullable = false)
    private String skillLevel; // 실력 (초급|중급|고급|상관없음)

    @Column(nullable = false)
    private String ageRange; // 연령대

    @Column(columnDefinition = "TEXT")
    private String description; // 그룹 설명

    @Column(nullable = false)
    private int maxParticipants; // 최대 참가 인원 수

    @Column(nullable = false)
    private String matchType; // 스크린/필드

    @Column(nullable = false)
    private boolean closed = false; // 모집 종료 여부

    @Column(nullable = false)
    private boolean deleted = false; // 소프트 삭제 여부
}