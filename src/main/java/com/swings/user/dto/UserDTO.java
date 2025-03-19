package com.swings.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("golfSkill")
    private String golfSkill; // Enum 대신 String 사용

    private String mbti;
    private String hobbies;
    private String religion;

    @JsonProperty("smoking")
    private String smoking; // Enum 대신 String 사용

    @JsonProperty("drinking")
    private String drinking; // Enum 대신 String 사용

    private String introduce;
    private String userImg;

    @JsonProperty("role")
    private String role; // Enum 대신 String 사용

    @JsonProperty("gender")
    private String gender; // Enum 대신 String 사용
}
