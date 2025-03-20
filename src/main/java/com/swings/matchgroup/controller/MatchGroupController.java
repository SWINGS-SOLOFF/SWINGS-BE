package com.swings.matchgroup.controller;

import com.swings.matchgroup.DTO.MatchGroupDTO;
import com.swings.matchgroup.service.MatchGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matchgroup")
@RequiredArgsConstructor
public class MatchGroupController {

    private final MatchGroupService matchGroupService;

    // 방 생성
    @PostMapping("/create")
    public ResponseEntity<MatchGroupDTO> createMatchGroup(@RequestBody MatchGroupDTO matchGroupDTO) {
        System.out.println("방 생성 API 호출 됨");
        MatchGroupDTO createdGroup = matchGroupService.createMatchGroup(matchGroupDTO);
        return ResponseEntity.ok(createdGroup);
    }

    // 모든 방 조회
    @GetMapping("/list")
    public ResponseEntity<List<MatchGroupDTO>> getAllPublicMatchGroups() {
        List<MatchGroupDTO> groups = matchGroupService.getAllPublicMatchGroups();
        return ResponseEntity.ok(groups);
    }

    // 특정 방 조회(지역, 성별, 일정 등...)
    @GetMapping("/{groupId}")
    public ResponseEntity<MatchGroupDTO> getMatchGroupById(@PathVariable Long groupId){
        MatchGroupDTO group = matchGroupService.getMatchGroupById(groupId);
        return ResponseEntity.ok(group);
    }

}
