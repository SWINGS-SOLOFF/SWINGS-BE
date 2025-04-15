package com.swings.matchgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.user.entity.UserEntity;
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


    // Entity → DTO 변환
    public static MatchParticipantDTO fromEntity(MatchParticipantEntity entity) {
        UserEntity user = entity.getUser();

        return MatchParticipantDTO.builder()
                .matchParticipantId(entity.getMatchParticipantId())
                .matchGroupId(entity.getMatchGroup().getMatchGroupId())
                .hostId(entity.getMatchGroup().getHost().getUserId())
                .userId(user.getUserId())
                .participantStatus(entity.getParticipantStatus().name())
                .joinAt(entity.getJoinAt())
                .username(user.getUsername())
                .name(user.getName())
                .mbti(user.getMbti())
                .job(user.getJob())
                .userImg(user.getUserImg())
                .gender(user.getGender().name())
                .age(user.getKoreanAge())
                .region(user.getActivityRegion().name())
                .build();
    }
}