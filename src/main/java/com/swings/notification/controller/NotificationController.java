package com.swings.notification.controller;

import com.swings.notification.dto.NotificationDTO;
import com.swings.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/notify")
    public void handleNotification(NotificationDTO notification){
        notificationService.sendNotification(notification);  // 특정 이벤트에 맞게 알림 전송
    }

}
