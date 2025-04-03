package com.swings.notification.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private String sender;  // 알림을 보내는 사람
    private String receiver;  // 알림을 받는 사람
    private String type;  // 알림 유형
    private String message;  // 알림 메세지

}
