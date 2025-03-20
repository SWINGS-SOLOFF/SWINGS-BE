package com.swings.matchgroup.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantDTO {

    private Long matchParticipantId;
    private Long matchGroupId;
    private Long userId;
    private String participantStatus;
    private LocalDateTime joinAt;

}
