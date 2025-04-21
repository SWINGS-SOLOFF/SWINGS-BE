package com.swings.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder //SocialServiceImpl에서 Builder 사용
@AllArgsConstructor //Builder쓸때 돕기위해 AllArgsConstructor 사용
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String name;

    private String birthDate;
    private String phonenumber;
    private String email; // 이메일
    private String job;

    private String golfSkill; //
    private String mbti;
    private String hobbies;
    private String religion;

    private String smoking; //
    private String drinking; //
    private String introduce;

    private String userImg; //

    private String role; //
    private String gender; //
    private String activityRegion;

    private Boolean isVerified; //
}
