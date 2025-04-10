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
    public ResponseEntity<List<MatchGroupDTO>> getAllMatchGroups() {
        log.info("공개된 그룹 목록 조회 API 호출 됨");
        List<MatchGroupDTO> groups = matchGroupService.getAllMatchGroups();
        return ResponseEntity.ok(groups);  // 공개된 방 리스트 반환
    }

    // 특정 그룹 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<MatchGroupDTO> getMatchGroupById(@PathVariable Long groupId){
        log.info("그룹 ID {} 조회 API 호출됨", groupId);
        MatchGroupDTO group = matchGroupService.getMatchGroupById(groupId);
        return ResponseEntity.ok(group);  // 특정 그룹 ID 조회
    }

    // 근처 그룹 찾기
    @GetMapping("/nearby")
    public ResponseEntity<List<MatchGroupDTO>> getNearbyGroups(
            @RequestParam double latitude,         // 내 위치 위도
            @RequestParam double longitude,        // 내 위치 경도
            @RequestParam(defaultValue = "5.0") double radiusInKm // 반경 거리 (기본 5km)
    ) {
        log.info("근처 그룹 조회 API 호출됨 - 위도: {}, 경도: {}, 반경: {}km", latitude, longitude, radiusInKm);
        List<MatchGroupDTO> nearbyGroups = matchGroupService.findNearbyGroups(latitude, longitude, radiusInKm);
        return ResponseEntity.ok(nearbyGroups);  // 반경 내 그룹 반환
    }
}
