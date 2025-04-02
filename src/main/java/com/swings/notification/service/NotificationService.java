package com.swings.notification.service;

import com.swings.notification.dto.NotificationDTO;

public interface NotificationService {
    void sendNotification(NotificationDTO notification);
}
