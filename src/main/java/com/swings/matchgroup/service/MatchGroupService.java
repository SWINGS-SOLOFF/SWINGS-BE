package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchGroupDTO;
import java.util.List;

public interface MatchGroupService {

    // 그룹 생성
    MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO);
    // 모든 그룹 보기
    List<MatchGroupDTO> getAllMatchGroups();
    // 그룹 찾기 By Id
    MatchGroupDTO getMatchGroupById(Long groupId);

}