package com.swings.matchgroup.repository;

import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipantEntity, Long> {

    // 특정 그룹의 참가자 목록 조회
    List<MatchParticipantEntity> findByMatchGroup(MatchGroupEntity matchGroup);

    // 특정 그룹에 참가한 사용자 여부 확인
    boolean existsByMatchGroup_MatchGroupIdAndUser_UserId(Long matchGroupId, Long userId);

    // MatchGroupId로 바로 조회하는 메서드(불필요한 Entity 조회 방지)
    List<MatchParticipantEntity> findByMatchGroup_MatchGroupId(Long matchGroupId);
}