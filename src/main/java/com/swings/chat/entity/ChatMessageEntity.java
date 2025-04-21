package com.swings.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Messages") // DB 테이블과 매핑
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId; // 메시지 ID (자동 증가)

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private ChatRoomEntity chatRoom; // 해당 메시지가 속한 채팅방 (객체 참조)

    @Column(nullable = false, length = 50)
    private String sender; // 메시지를 보낸 사용자

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 메시지 내용

    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt; // 메시지 전송 시간

    @Column(nullable = false)
    private boolean isRead; // 읽었는지 여부 (기본값은 false)


    // 자동으로 현재 시간 설정
    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
        this.isRead = false; // 새 메시지는 기본적으로 읽지 않은 상태

    }
}
