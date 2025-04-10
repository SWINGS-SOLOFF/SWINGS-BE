package com.swings.matchgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupDTO {

    @JsonProperty("matchGroupId")
    private Long matchGroupId;

    @JsonProperty("hostId")
    private Long hostId;

    @JsonProperty("hostUsername")
    private String hostUsername;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("location")
    private String location;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("schedule")
    private String schedule;

    @JsonProperty("playStyle")
    private String playStyle;

    @JsonProperty("genderRatio")
    private String genderRatio;

    @JsonProperty("skillLevel")
    private String skillLevel;

    @JsonProperty("ageRange")
    private String ageRange;

    @JsonProperty("description")
    private String description;

    @JsonProperty("maxParticipants")
    private int maxParticipants;

    @JsonProperty("matchType")
    private String matchType;

    // Entity → DTO 변환 (일반 조회용)
    public static MatchGroupDTO fromEntity(MatchGroupEntity entity) {
        return MatchGroupDTO.builder()
                .matchGroupId(entity.getMatchGroupId())
                .hostId(entity.getHost().getUserId())
                .hostUsername(entity.getHost().getUsername())
                .groupName(entity.getGroupName())
                .location(entity.getLocation())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .schedule(entity.getSchedule())
                .playStyle(entity.getPlayStyle())
                .genderRatio(entity.getGenderRatio())
                .skillLevel(entity.getSkillLevel())
                .ageRange(entity.getAgeRange())
                .description(entity.getDescription())
                .maxParticipants(entity.getMaxParticipants())
                .matchType(entity.getMatchType())
                .build();
    }

    // DTO → Entity 변환 (그룹 생성 시 사용)
    public MatchGroupEntity toEntity(UserEntity host) {
        return MatchGroupEntity.builder()
                .host(host)
                .groupName(groupName)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .schedule(schedule)
                .playStyle(playStyle)
                .genderRatio(genderRatio)
                .skillLevel(skillLevel)
                .ageRange(ageRange)
                .description(description)
                .maxParticipants(maxParticipants)
                .matchType(matchType)
                .build();
    }

    // Projection → DTO 변환 (근처 그룹 조회 전용)
    public static MatchGroupDTO fromProjection(MatchGroupNearbyProjection projection) {
        return MatchGroupDTO.builder()
                .matchGroupId(projection.getMatchGroupId())
                .groupName(projection.getGroupName())
                .location(projection.getLocation())
                .latitude(projection.getLatitude())
                .longitude(projection.getLongitude())
                .schedule(projection.getSchedule())
                .hostUsername(projection.getHostUsername()) // hostId는 Projection에 없음
                .build();
    }
}
