package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;

import java.util.List;

public interface MatchParticipantService {

    // 참가 신청
    MatchParticipantDTO joinMatch(Long matchGroupId, Long userId);

    // 참가 신청 취소
    void leaveMatch(Long matchGroupId, Long userId);

    // 참가 승인 (방장)
    void approveParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId);

    // 참가 거절 (방장)
    void rejectParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId);

    // 참가자 강퇴 (방장)
    void removeParticipant(Long matchGroupId, Long userId, Long hostUserId);

    // 확정된 참가자 조회
    List<MatchParticipantDTO> getAcceptedParticipants(Long matchGroupId);

    // 신청중인 참가자 조회
    List<MatchParticipantDTO> getPendingParticipants(Long matchGroupId);

    // 내 참가/신청/이력 조회
    List<MatchParticipantDTO> getMyGroups(MatchParticipantDTO request);

    // 확정된 참가자 수 조회
    int countAcceptedParticipants(Long matchGroupId);

    // 확정 참가자 그룹 나가기 (방장: 그룹 삭제)
    void leaveAcceptedGroup(Long matchGroupId, Long userId);

    // 참가 가능 여부 확인
    boolean canUserJoinGroup(Long matchGroupId, Long userId);
}