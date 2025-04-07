package com.swings.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentLikeDTO {
    private String username;   // 상대방 유저 아이디
    private String name;       // 상대방 이름
    private String userImg;    // 상대방 프로필 이미지
    private boolean isMutual;  // 쌍방 좋아요 여부
}
