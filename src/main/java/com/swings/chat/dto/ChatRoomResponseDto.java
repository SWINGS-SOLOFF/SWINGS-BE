// ChatRoomResponseDto.java
package com.swings.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private String user1;
    private String user2;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    private Long unreadCount;
}
