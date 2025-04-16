package com.swings.notification.controller;

import com.swings.notification.dto.NotificationDTO;
import com.swings.notification.entity.NotificationEntity;
import com.swings.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // WebSocket 수신
    @MessageMapping("/notify")
    public void handleNotification(NotificationDTO notification){
        notificationService.sendNotification(notification);
    }

    // 전체 알림 내역 조회
    @GetMapping("/list")
    public List<NotificationDTO> getAllNotifications(@RequestParam String receiver) {
        return notificationService.getNotificationsByReceiver(receiver).stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 알림 읽음 처리
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("알림 읽음 처리 완료");
    }

    // 알림 삭제
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("알림 삭제 완료");
    }


}
