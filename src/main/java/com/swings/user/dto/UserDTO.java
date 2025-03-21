package com.swings.user.dto;

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
    private String phone_number;
    private String job;

    private String golf_skill; // 🔹 Enum 대신 String 사용
    private String mbti;
    private String hobbies;
    private String religion;

    private String smoking; // 🔹 Enum 대신 String 사용
    private String drinking; // 🔹 Enum 대신 String 사용
    private String introduce;

    private String user_img; // 🔹 Base64 이미지 저장

    private String role; // 🔹 Enum 대신 String 사용
    private String gender; // 🔹 Enum 대신 String 사용
}
