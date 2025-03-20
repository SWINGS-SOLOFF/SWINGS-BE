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





    // Entity → DTO 변환
    public static UserSelectDTO fromEntity(UserEntity user) {
        return UserSelectDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername()) // 변경됨
                .name(user.getName())
                .gender(user.getGender().name())  // Enum → String 변환
                .userImg(user.getUserImg())
                .build();
    }
}
