package com.swings.user.dto;

import com.swings.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String name;
    private String phonenumber;
    private String job;
    private UserEntity.GolfSkill golfSkill;
    private String mbti;
    private String hobbies;
    private String religion;
    private UserEntity.YesNo smoking;
    private UserEntity.YesNo drinking;
    private String introduce;
    private String userImg;
    private UserEntity.Role role;
    private UserEntity.Gender gender;
}
