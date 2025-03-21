package com.swings.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSelectDTO {
    private Long userId;
    private String username;
    private String name;
    private String gender;
    private String userImg;
}
