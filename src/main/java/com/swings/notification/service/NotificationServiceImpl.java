package com.swings.notification.service;

import com.swings.notification.dto.NotificationDTO;
import com.swings.notification.entity.NotificationEntity;
import com.swings.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    // 실시간 알림 전송
    @Override
    public void sendNotification(NotificationDTO notification) {

        // 실시간 전송
        messagingTemplate.convertAndSend("/topic/notification/" + notification.getReceiver(), notification);

        // DB 저장
        NotificationEntity entity = NotificationEntity.builder()
                .sender(notification.getSender())
                .receiver(notification.getReceiver())
                .type(notification.getType())
                .message(notification.getMessage())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();

        notificationRepository.save(entity);
    }

    // 참가 신청 알림
    public void notifyHostOnJoinRequest(String groupName, String hostUsername, String applicantUsername) {
        NotificationDTO notification = NotificationDTO.builder()
                .sender(applicantUsername)
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

    // 알림 저장
    public List<NotificationEntity> getNotificationsByReceiver(String receiver) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // 알림 삭제
    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

}
