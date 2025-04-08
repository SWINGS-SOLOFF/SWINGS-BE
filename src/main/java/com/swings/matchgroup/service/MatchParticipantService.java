package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import java.util.List;


public interface MatchParticipantService {

    // 참가 신청
    MatchParticipantDTO joinMatch(Long matchGroupId, Long userId);

    // 참가 신청 취소
    void leaveMatch(Long matchGroupId, Long userId);

    // 참가 신청 승인(방장)
    void approveParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId);

    // 참가 신청 거절(방장)
    void rejectParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId);

    // 참가자 강퇴
    void removeParticipant(Long matchGroupId, Long userId, Long hostUserId);

    // 특정 방의 참가 신청자 목록 조회
    List<MatchParticipantDTO> getParticipantsByMatchGroupId(Long matchGroupId);

    // 특정 방의 참가자 목록 조회
    List<MatchParticipantDTO> getAcceptedParticipants(Long matchGroupId);
}
