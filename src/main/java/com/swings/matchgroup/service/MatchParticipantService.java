package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import java.util.List;


public interface MatchParticipantService {

    // 참가 신청
    MatchParticipantDTO joinMatch(Long groupId, String username);
    // 특정 방의 참가자 목록 조회
    List<MatchParticipantDTO> getParticipantsByGroupId(Long groupId);

}
