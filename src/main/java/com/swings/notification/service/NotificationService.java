package com.swings.notification.service;

import com.swings.notification.dto.NotificationDTO;
import com.swings.notification.entity.NotificationEntity;

import java.util.List;

public interface NotificationService {

    // 실시간 알림 전송
    void sendNotification(NotificationDTO notification);

    // 참가 신청 알림
    void notifyHostOnJoinRequest(String groupName, String hostUsername, String applicantUsername);

    // 참가 승인 알림
    void notifyUserOnApproval(String groupName, String receiverUsername);

    // 참가 거절 알림
    void notifyUserOnRejection(String groupName, String receiverUsername);
    
    // 알림 저장
    List<NotificationEntity> getNotificationsByReceiver(String receiver);

    // 알림 읽음 처리
    void markAsRead(Long notificationId);

    // 알림 삭제
    void deleteNotification(Long notificationId);

}
