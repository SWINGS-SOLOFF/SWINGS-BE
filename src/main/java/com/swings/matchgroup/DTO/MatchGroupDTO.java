package com.swings.matchgroup.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupDTO {

    private Long matchGroupId;
    private String username;
    private String location;
    private String schedule;
    private String playStyle;
    private String genderRatio;
    private String skillLevel;
    private String ageRange;
    private String additionalOptions;
    private Boolean isPublic;
    private int maxParticipants;

}
