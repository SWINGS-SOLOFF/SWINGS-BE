package com.swings.matchgroup.service;

import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.matchgroup.repository.MatchParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;

    // 참가 신청
    public MatchParticipantEntity joinMatch(MatchParticipantEntity participant){
        return matchParticipantRepository.save(participant);
    }

    // 특정 방의 참가자 목록 조회
    public List<MatchParticipantEntity> getParticipantsByGroupId(String username){
        return matchParticipantRepository.findByMatchGroup_Username(username);
    }
}
