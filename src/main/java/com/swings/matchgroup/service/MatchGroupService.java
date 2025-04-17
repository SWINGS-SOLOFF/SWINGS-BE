package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchGroupDTO;
import java.util.List;

public interface MatchGroupService {

    // 그룹 생성
    MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO);

    // 모든 그룹 보기
    List<MatchGroupDTO> getAllMatchGroups();

    // 그룹 찾기
    MatchGroupDTO getMatchGroupById(Long groupId);

    // 근처 그룹 찾기
    List<MatchGroupDTO> findNearbyGroups(double latitude, double longitude, double radiusInKm);

    // 내가 방장인 그룹 찾기
    List<MatchGroupDTO> getGroupsByHost(Long hostId);

    // 모집 상태 변경
    void updateGroupStatus(Long groupId, boolean closed);

    // 그룹 삭제 (방장만 가능)
    void deleteGroup(Long groupId, Long userId);
}