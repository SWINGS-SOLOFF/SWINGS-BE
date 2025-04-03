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
    @JoinColumn(name = "host_id", nullable = false) // DB 컬럼명 명시
    private UserEntity host; // 방장 (그룹 생성자)

    @Column(nullable = true)
    private String groupName; // 그룹 이름

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
    private String description; // 그룹 설명

    @Column(nullable = false)
    private int maxParticipants; // 최대 인원 수

    @Column(nullable = false)
    private String matchType;

}
