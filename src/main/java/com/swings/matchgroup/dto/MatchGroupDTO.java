package com.swings.matchgroup.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupDTO {

    private Long matchGroupId;
    private String groupName;
    private String location;
    private String schedule;
    private String playStyle;
    private String genderRatio;
    private String skillLevel;
    private String ageRange;
    private String description;
    private int maxParticipants;
    private String matchType;

}
