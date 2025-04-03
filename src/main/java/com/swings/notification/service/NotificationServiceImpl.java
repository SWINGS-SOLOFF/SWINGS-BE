package com.swings.notification.service;

import com.swings.notification.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/notification/" + notification.getReceiver(), notification);
    }

    // 참가 신청 알림
    public void notifyHostOnJoinRequest(String groupName, String hostUsername, String applicantUsername) {
        NotificationDTO notification = NotificationDTO.builder()
                .receiver(hostUsername)
                .message(applicantUsername + " 님이 [" + groupName + "]에 참가를 신청했습니다.")
                .type("JOIN_REQUEST")
                .build();

        sendNotification(notification);
    }

    // 참가 승인 알림
    public void notifyUserOnApproval(String groupName, String receiverUsername) {
        NotificationDTO notification = NotificationDTO.builder()
                .receiver(receiverUsername)
                .message("[" + groupName + "] 참가가 승인되었습니다.")
                .type("APPROVED")
                .build();

        sendNotification(notification);
    }

    // 참가 거절 알림
    public void notifyUserOnRejection(String groupName, String receiverUsername) {
        NotificationDTO notification = NotificationDTO.builder()
                .receiver(receiverUsername)
                .message("[" + groupName + "] 참가가 거절되었습니다.")
                .type("REJECTED")
                .build();

        sendNotification(notification);
    }
}
