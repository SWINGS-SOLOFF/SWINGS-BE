package com.swings.notification.dto;

import com.swings.notification.entity.NotificationEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long notificationId;
    private String sender;  // 알림을 보내는 사람
    private String receiver;  // 알림을 받는 사람
    private String type;  // 알림 유형
    private String message;  // 알림 메세지
    private boolean isRead;  // 읽음 여부

    
    // Entity → DTO 변환 메서드
    public static NotificationDTO fromEntity(NotificationEntity entity) {
        return NotificationDTO.builder()
                .notificationId(entity.getNotificationId())
                .sender(entity.getSender())
                .receiver(entity.getReceiver())
                .type(entity.getType())
                .message(entity.getMessage())
                .isRead(entity.isRead())
                .build();
    }
}
