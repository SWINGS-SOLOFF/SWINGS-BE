package com.swings.matchgroup.controller;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import com.swings.matchgroup.service.MatchParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 참가 신청 승인(방장)
    @PostMapping("/approve")
    public ResponseEntity<String> approveParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.approveParticipant(
                dto.getMatchGroupId(),
                dto.getMatchParticipantId(),
                dto.getUserId()
        );
        return ResponseEntity.ok("참가 승인 완료");
    }

    // 참가 신청 거절(방장)
    @PostMapping("/reject")
    public ResponseEntity<String> rejectParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.rejectParticipant(
                dto.getMatchGroupId(),
                dto.getMatchParticipantId(),
                dto.getUserId()
        );
        return ResponseEntity.ok("참가 거절 완료");
    }

    // 참가자 강퇴
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeParticipant(@RequestBody MatchParticipantDTO dto) {
        matchParticipantService.removeParticipant(dto.getMatchGroupId(), dto.getUserId(), dto.getHostId());
        return ResponseEntity.ok("강퇴 완료");
    }

    // 특정 방의 참가 신청자 목록 조회(방장)
    @GetMapping("/list/{matchGroupId}")
    public ResponseEntity<List<MatchParticipantDTO>> getParticipantsByMatchGroupId(
            @PathVariable("matchGroupId") Long matchGroupId) {
        return ResponseEntity.ok(
                matchParticipantService.getParticipantsByMatchGroupId(matchGroupId)
        );
    }

    // 특정 방의 참가자 목록 조회
    @GetMapping("/accepted/{matchGroupId}")
    public ResponseEntity<List<MatchParticipantDTO>> getAcceptedParticipants(
            @PathVariable("matchGroupId") Long matchGroupId) {
        return ResponseEntity.ok(
                matchParticipantService.getAcceptedParticipants(matchGroupId)
        );
    }

}
