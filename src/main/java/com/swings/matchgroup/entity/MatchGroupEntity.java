package com.swings.matchgroup.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matchgroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchGroupId;

    @Column(nullable = false)
    private String username; // 유저 이름 필드

    @Column(nullable = false)
    private String location; // 골프장 장소

    @Column(nullable = false)
    private String schedule; // 일정 (날짜, 시간)

    @Column(nullable = false)
    private String playStyle; // 플레이 스타일(유쾌한|평범|진지한)

    @Column(nullable = false)
    private String genderRatio; // 성비

    @Column(nullable = false)
    private String skillLevel; // 실력(초급|중급|고급|상관없음)

    @Column(nullable = false)
    private String ageRange; // 연령

    @Column
    private String additionalOptions; // 기타 옵션

    @Column(nullable = false)
    private Boolean isPublic; // 방 공개 여부

    @Column(nullable = false)
    private int maxParticipants; // 최대 인원 수



}
