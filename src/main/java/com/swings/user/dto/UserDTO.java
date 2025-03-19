package com.swings.user.dto;

import com.swings.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String username;
    private String name;
    private String gender;
    private String userImg;

    // Entity → DTO 변환
    public static UserDTO fromEntity(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername()) // 변경됨
                .name(user.getName())
                .gender(user.getGender())
                .userImg(user.getUserImg())
                .build();
    }
}
