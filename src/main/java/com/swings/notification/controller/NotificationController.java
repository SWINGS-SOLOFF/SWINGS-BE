package com.swings.notification.controller;

import com.swings.notification.dto.NotificationDTO;
import com.swings.notification.entity.NotificationEntity;
import com.swings.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    // WebSocket 수신
    @MessageMapping("/notify")
    public void handleNotification(NotificationDTO notification){
        notificationService.sendNotification(notification);  // 특정 이벤트에 맞게 알림 전송
    }

    // 전체 알림 내역 조회
    @GetMapping("/list")
    public List<NotificationEntity> getAllNotifications(@RequestParam String receiver) {
        return notificationService.getNotificationsByReceiver(receiver);
    }

}
