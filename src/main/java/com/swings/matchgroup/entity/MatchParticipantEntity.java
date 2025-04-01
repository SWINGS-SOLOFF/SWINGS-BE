package com.swings.matchgroup.entity;

import com.swings.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchParticipant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long matchParticipantId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MatchGroupEntity matchGroup;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus participantStatus;

    @Column
    private LocalDateTime joinAt;

    public enum ParticipantStatus {
        PENDING, ACCEPTED, REJECTED
    }

}
