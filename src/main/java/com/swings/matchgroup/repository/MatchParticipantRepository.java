package com.swings.matchgroup.repository;

import com.swings.matchgroup.entity.MatchParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipantEntity, Long> {

    // 참가 여부 확인
    boolean existsByMatchGroup_MatchGroupIdAndUser_UserId(Long matchGroupId, Long userId);

    // 전체 참가자 조회 (상태 무관)
    List<MatchParticipantEntity> findByMatchGroupMatchGroupId(Long matchGroupId);

    // 상태별 참가자 조회
    List<MatchParticipantEntity> findByMatchGroupMatchGroupIdAndParticipantStatus(
            Long matchGroupId,
            MatchParticipantEntity.ParticipantStatus participantStatus
    );

    // 특정 사용자의 모든 이력 조회
    List<MatchParticipantEntity> findByUser_UserId(Long userId);

    // 특정 사용자의 상태별 이력 조회
    List<MatchParticipantEntity> findByUser_UserIdAndParticipantStatus(
            Long userId,
            MatchParticipantEntity.ParticipantStatus status
    );

    // 확정된 참가자 수 조회
    int countByMatchGroup_MatchGroupIdAndParticipantStatus(
            Long matchGroupId,
            MatchParticipantEntity.ParticipantStatus status
    );
}