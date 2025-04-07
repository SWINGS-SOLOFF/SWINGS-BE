package com.swings.matchgroup.repository;

import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipantEntity, Long> {

    // 특정 그룹에 참가한 사용자 여부 확인
    boolean existsByMatchGroup_MatchGroupIdAndUser_UserId(Long matchGroupId, Long userId);

    // 특정 그룹의 전체 참가자 조회 (Entity 불러오지 않고 ID로 조회)
    List<MatchParticipantEntity> findByMatchGroupMatchGroupId(Long matchGroupId);

    // 특정 그룹의 승인된 참가자만 조회
    List<MatchParticipantEntity> findByMatchGroupMatchGroupIdAndParticipantStatus(
            Long matchGroupId,
            MatchParticipantEntity.ParticipantStatus participantStatus
    );
}