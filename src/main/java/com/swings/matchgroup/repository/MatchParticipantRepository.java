package com.swings.matchgroup.repository;

import com.swings.matchgroup.entity.MatchParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchParticipantRepository extends JpaRepository<MatchParticipantEntity, Long> {
    List<MatchParticipantEntity> findByMatchGroup_Username(String username);
}