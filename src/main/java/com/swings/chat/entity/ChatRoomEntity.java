package com.swings.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "ChatRooms")
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;  // 채팅방 ID

    private String user1; // 유저1
    private String user2; // 유저2

    private LocalDateTime createdAt; // 생성 시간

    //  새롭게 추가하는 생성자 (user1, user2만 받음)
    public ChatRoomEntity(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = LocalDateTime.now(); // 생성 시간 자동 세팅
    }
}
