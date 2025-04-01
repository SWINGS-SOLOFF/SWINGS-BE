package com.swings.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String name;

    @JsonProperty("birthdate")
    private String birthDate;
    private String phonenumber;
    private String email; // 이메일
    private String job;

    private String golfSkill; // 🔹 Enum 대신 String 사용
    private String mbti;
    private String hobbies;
    private String religion;

    private String smoking; // 🔹 Enum 대신 String 사용
    private String drinking; // 🔹 Enum 대신 String 사용
    private String introduce;

    private String userImg; // 🔹 Base64 이미지 저장

    private String role; // 🔹 Enum 대신 String 사용
    private String gender; // 🔹 Enum 대신 String 사용
    private String activityRegion;
}
