package com.swings.matchgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.user.entity.UserEntity;
import lombok.*;

import java.util.List;

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

    @JsonProperty("participants")
    private List<MatchParticipantDTO> participants;

    private String groupName;
    private String location;
    private Double latitude;
    private Double longitude;
    private String schedule;
    private String playStyle;
    private int femaleLimit;
    private int maleLimit;
    private String skillLevel;
    private String ageRange;
    private String description;
    private int maxParticipants;
    private String matchType;

    // Entity → DTO (with participants)
    public static MatchGroupDTO fromEntity(MatchGroupEntity entity, List<MatchParticipantDTO> participants) {
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
                .femaleLimit(entity.getFemaleLimit())
                .maleLimit(entity.getMaleLimit())
                .skillLevel(entity.getSkillLevel())
                .ageRange(entity.getAgeRange())
                .description(entity.getDescription())
                .maxParticipants(entity.getMaxParticipants())
                .matchType(entity.getMatchType())
                .participants(participants)
                .build();
    }

    // DTO → Entity
    public MatchGroupEntity toEntity(UserEntity host) {
        return MatchGroupEntity.builder()
                .host(host)
                .groupName(groupName)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .schedule(schedule)
                .playStyle(playStyle)
                .femaleLimit(femaleLimit)
                .maleLimit(maleLimit)
                .skillLevel(skillLevel)
                .ageRange(ageRange)
                .description(description)
                .maxParticipants(maxParticipants)
                .matchType(matchType)
                .build();
    }

    // Projection → DTO (근처 그룹 전용)
    public static MatchGroupDTO fromProjection(MatchGroupNearbyProjection projection) {
        return MatchGroupDTO.builder()
                .matchGroupId(projection.getMatchGroupId())
                .groupName(projection.getGroupName())
                .location(projection.getLocation())
                .latitude(projection.getLatitude())
                .longitude(projection.getLongitude())
                .schedule(projection.getSchedule())
                .hostUsername(projection.getHostUsername())
                .build();
    }
}