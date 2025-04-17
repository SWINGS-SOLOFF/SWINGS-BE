package com.swings.matchgroup.controller;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import com.swings.matchgroup.service.MatchParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matchParticipant")
@RequiredArgsConstructor
public class MatchParticipantController {

    private final MatchParticipantService matchParticipantService;

    // 참가 신청
    @PostMapping("/join")
    public ResponseEntity<MatchParticipantDTO> joinMatch(@RequestBody MatchParticipantDTO dto) {
        return ResponseEntity.ok(
                matchParticipantService.joinMatch(dto.getMatchGroupId(), dto.getUserId())
        );
    }

    // 참가 신청 취소
    @PostMapping("/leave")
    public ResponseEntity<String> leaveMatch(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.leaveMatch(dto.getMatchGroupId(), dto.getUserId());
        return ResponseEntity.ok("참가 취소 완료");
    }

    // 참가 승인 (방장)
    @PostMapping("/approve")
    public ResponseEntity<String> approveParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.approveParticipant(
                dto.getMatchGroupId(),
                dto.getMatchParticipantId(),
                dto.getUserId()
        );
        return ResponseEntity.ok("참가 승인 완료");
    }

    // 참가 거절 (방장)
    @PostMapping("/reject")
    public ResponseEntity<String> rejectParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.rejectParticipant(
                dto.getMatchGroupId(),
                dto.getMatchParticipantId(),
                dto.getUserId()
        );
        return ResponseEntity.ok("참가 거절 완료");
    }

    // 강퇴 (방장)
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.removeParticipant(dto.getMatchGroupId(), dto.getUserId(), dto.getHostId());
        return ResponseEntity.ok("강퇴 완료");
    }

    // 확정된 참가자 조회 (ACCEPTED)
    @GetMapping("/accepted/{matchGroupId}")
    public ResponseEntity<List<MatchParticipantDTO>> getAcceptedParticipants(@PathVariable Long matchGroupId) {
        return ResponseEntity.ok(
                matchParticipantService.getAcceptedParticipants(matchGroupId)
        );
    }

    // 신청중인 참가자 조회 (PENDING)
    @GetMapping("/pending/{matchGroupId}")
    public ResponseEntity<List<MatchParticipantDTO>> getPendingParticipants(@PathVariable Long matchGroupId) {
        return ResponseEntity.ok(
                matchParticipantService.getPendingParticipants(matchGroupId)
        );
    }

    // 나의 이력 조회
    @PostMapping("/my")
    public ResponseEntity<List<MatchParticipantDTO>> getMyGroups(@RequestBody MatchParticipantDTO request) {
        return ResponseEntity.ok(matchParticipantService.getMyGroups(request));
    }

    // 확정된 인원 수 조회
    @GetMapping("/accepted/count/{matchGroupId}")
    public ResponseEntity<Integer> getAcceptedCount(@PathVariable Long matchGroupId) {
        return ResponseEntity.ok(matchParticipantService.countAcceptedParticipants(matchGroupId));
    }

    // 그룹 나가기 (방장: 그룹 삭제)
    @PostMapping("/leave/accepted")
    public ResponseEntity<String> leaveAcceptedGroup(@RequestBody Map<String, Long> body) {
        Long matchGroupId = body.get("matchGroupId");
        Long userId = body.get("userId");
        matchParticipantService.leaveAcceptedGroup(matchGroupId, userId);
        return ResponseEntity.ok("그룹에서 나갔습니다.");
    }

    // 참가 가능 여부 확인
    @GetMapping("/check/{matchGroupId}/{userId}")
    public ResponseEntity<Boolean> canUserJoin(
            @PathVariable Long matchGroupId,
            @PathVariable Long userId
    ) {
        boolean allowed = matchParticipantService.canUserJoinGroup(matchGroupId, userId);
        return ResponseEntity.ok(allowed);
    }
}