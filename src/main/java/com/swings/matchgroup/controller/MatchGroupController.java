package com.swings.matchgroup.controller;

import com.swings.matchgroup.dto.MatchGroupDTO;
import com.swings.matchgroup.service.MatchGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matchgroup")
@RequiredArgsConstructor
@Slf4j
public class MatchGroupController {

    private final MatchGroupService matchGroupService;

    // 그룹 생성
    @PostMapping("/create")
    public ResponseEntity<MatchGroupDTO> createMatchGroup(@RequestBody MatchGroupDTO matchGroupDTO) {
        log.info("그룹 생성 API 호출 됨: {}", matchGroupDTO);
        MatchGroupDTO createdGroup = matchGroupService.createMatchGroup(matchGroupDTO);
        return ResponseEntity.ok(createdGroup);  // 생성된 그룹 반환
    }

    // 모든 그룹 조회
    @GetMapping("/list")
    public ResponseEntity<List<MatchGroupDTO>> getAllPublicMatchGroups() {
        log.info("공개된 그룹 목록 조회 API 호출 됨");
        List<MatchGroupDTO> groups = matchGroupService.getAllPublicMatchGroups();
        return ResponseEntity.ok(groups);  // 공개된 방 리스트 반환
    }

    // 특정 그룹 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<MatchGroupDTO> getMatchGroupById(@PathVariable Long groupId){
        log.info("그룹 ID {} 조회 API 호출됨", groupId);
        MatchGroupDTO group = matchGroupService.getMatchGroupById(groupId);
        return ResponseEntity.ok(group);  // 특정 그룹 ID 조회
    }

}
