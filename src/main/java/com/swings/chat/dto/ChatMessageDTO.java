package com.swings.chat.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long roomId;  // 채팅방 ID
    private String sender;  // 보낸 사람
    private String content; // 메시지 내용
    private String senderName;
    private LocalDateTime sentAt;
}
