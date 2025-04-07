package com.swings.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NotificationId;

    private String sender;  // 알림을 보내는 사람
    private String receiver;  // 알림을 받는 사람
    private String type;  // 알림 유형

    @Column(length = 1000)
    private String message;  // 알림 메세지

    private LocalDateTime createdAt;

    private boolean isRead;  // 읽음 여부

}
