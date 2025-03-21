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

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("participantStatus")
    private String participantStatus;

    @JsonProperty("joinAt")
    private LocalDateTime joinAt;

    public static MatchParticipantDTO fromEntity(MatchParticipantEntity entity) {
        return MatchParticipantDTO.builder()
                .matchParticipantId(entity.getMatchParticipantId())
                .matchGroupId(entity.getMatchGroup().getMatchGroupId())
                .userId(entity.getUser().getUserId())
                .participantStatus(entity.getParticipantStatus().name())
                .joinAt(entity.getJoinAt())
                .build();
    }

}
