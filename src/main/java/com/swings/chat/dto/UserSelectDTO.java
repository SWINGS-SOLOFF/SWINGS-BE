package com.swings.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSelectDTO {
    private Long userId;
    private String username;
    private String name;
    private String gender;
    private String userImg;
    private String introduce;
    private String activityRegion;
    private String targetUserImg;

}
