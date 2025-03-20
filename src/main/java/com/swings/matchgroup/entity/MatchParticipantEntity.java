package com.swings.matchgroup.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchparticipant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchParticipantId;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    private MatchGroupEntity matchGroup;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_status", nullable = false)
    private ParticipantStatus participantStatus;

    @Column(name = "join_at")
    private LocalDateTime joinAt;

    public enum ParticipantStatus {
        PENDING, ACCEPTED, REJECTED
    }

}
