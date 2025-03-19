package com.swings.matchgroup.controller;

import com.swings.matchgroup.entity.MatchParticipantEntity;
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
    public ResponseEntity<MatchParticipantEntity> joinMatch(@RequestBody MatchParticipantEntity participant) {
        MatchParticipantEntity newParticipant = matchParticipantService.joinMatch(participant);
        return ResponseEntity.ok(newParticipant);
    }

    // 특정 방 참가자 목록 조회
    @GetMapping("/list/{groupUsername}")
    public ResponseEntity<List<MatchParticipantEntity>> getParticipantsByGroupUsername(@PathVariable String username) {
        List<MatchParticipantEntity> participants = matchParticipantService.getParticipantsByGroupId(username);
        return ResponseEntity.ok(participants);
    }
}
