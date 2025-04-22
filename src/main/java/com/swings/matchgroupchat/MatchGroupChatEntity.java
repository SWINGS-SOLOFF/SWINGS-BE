package com.swings.matchgroupchat;

import com.swings.matchgroup.entity.MatchGroupEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchGroupChat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchGroupChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_group_id", nullable = false)
    private MatchGroupEntity matchGroup;

    @Column(nullable = false, length = 50)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private boolean isRead;

    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }
}
