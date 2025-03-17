package com.swings.chat.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageEntity {

    private String roomId; // 채팅방 Id
    private String sender; // 보낸 사람
    private String message; // 메시지 내용

}
