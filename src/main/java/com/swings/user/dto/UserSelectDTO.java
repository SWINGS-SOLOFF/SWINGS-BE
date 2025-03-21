package com.swings.user.dto;

import com.swings.user.entity.UserEntity;
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



}
