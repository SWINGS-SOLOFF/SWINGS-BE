package com.swings.matchgroup.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_participant_id")
    private Long matchParticipantId;

    @ManyToOne
    @JoinColumn(name = "match_group_id", nullable = false)
    private MatchGroupEntity matchGroup;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
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
