package com.swings.matchgroup.controller;

import com.swings.matchgroup.service.MatchGroupService;
import com.swings.matchgroup.entity.MatchGroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matchGroup")
@RequiredArgsConstructor
public class MatchGroupController {

    private final MatchGroupService matchGroupService;

    // 방 생성
    @PostMapping("/create")
    public ResponseEntity<MatchGroupEntity> createMatchGroup(@RequestBody MatchGroupEntity matchGroup) {
        System.out.println("방 생성 API 호출 됨");
        MatchGroupEntity createdGroup = matchGroupService.createMatchGroup(matchGroup);
        return ResponseEntity.ok(createdGroup);  // MatchGroupEntity를 직접 반환
    }

    // 모든 방 조회
    @GetMapping("/list")
    public ResponseEntity<List<MatchGroupEntity>> getAllPublicMatchGroups(){
        List<MatchGroupEntity> groups = matchGroupService.getAllPublicMatchGroups();
        return ResponseEntity.ok(groups);
    }

    // 특정 방 조회(지역, 성별, 일정 등...)
    @GetMapping("/{groupId}")
    public ResponseEntity<Optional<MatchGroupEntity>> getMatchGroupById(@PathVariable Long groupId){
        Optional<MatchGroupEntity> group = matchGroupService.getMatchGroupById(groupId);
        return ResponseEntity.ok(group);
    }

}
