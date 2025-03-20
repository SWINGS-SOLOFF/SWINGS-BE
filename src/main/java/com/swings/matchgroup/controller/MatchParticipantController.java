package com.swings.matchgroup.controller;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import com.swings.matchgroup.service.MatchParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matchParticipant")
@RequiredArgsConstructor
public class MatchParticipantController {

    private final MatchParticipantService matchParticipantService;

    // 참가 신청
    @PostMapping("/join")
    public ResponseEntity<MatchParticipantDTO> joinMatch(@RequestParam Long groupId, @RequestParam String username) {
        MatchParticipantDTO newParticipant = matchParticipantService.joinMatch(groupId, username);
        return ResponseEntity.ok(newParticipant);
    }

    // 특정 방 참가자 목록 조회
    @GetMapping("/list/{groupId}")
    public ResponseEntity<List<MatchParticipantDTO>> getParticipantsByGroupId(@PathVariable Long groupId) {
        List<MatchParticipantDTO> participants = matchParticipantService.getParticipantsByGroupId(groupId);
        return ResponseEntity.ok(participants);
    }
}
