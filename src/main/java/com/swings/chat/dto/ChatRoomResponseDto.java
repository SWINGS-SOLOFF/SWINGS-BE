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

    //  프론트에서 사용할 상대방 정보
    private String targetName;
    private String targetUsername;
    private String targetImg;
}
