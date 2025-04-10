package com.swings.matchgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantDTO {

    @JsonProperty("matchParticipantId")
    private Long matchParticipantId;

    @JsonProperty("matchGroupId")
    private Long matchGroupId;

    @JsonProperty("hostId")
    private Long hostId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("participantStatus")
    private String participantStatus;

    @JsonProperty("joinAt")
    private LocalDateTime joinAt;

    // 사용자 정보
    @JsonProperty("username")
    private String username;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mbti")
    private String mbti;

    @JsonProperty("job")
    private String job;

    @JsonProperty("userImg")
    private String userImg;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    private int age;

    @JsonProperty("region")
    private String region;


    // Entity → DTO 변환 (기본 필드만 포함)
    public static MatchParticipantDTO fromEntity(MatchParticipantEntity entity) {
        return MatchParticipantDTO.builder()
                .matchParticipantId(entity.getMatchParticipantId())
                .matchGroupId(entity.getMatchGroup().getMatchGroupId())
                .hostId(entity.getMatchGroup().getHost().getUserId())
                .userId(entity.getUser().getUserId())
                .participantStatus(entity.getParticipantStatus().name())
                .joinAt(entity.getJoinAt())
                .build();
    }
}
