package com.swings.matchgroup.service;

import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchGroupService {

    private final MatchGroupRepository matchGroupRepository;

    // 방 생성
    public MatchGroupEntity createMatchGroup(MatchGroupEntity matchGroup) {
        return matchGroupRepository.save(matchGroup);
    }

    // 공개된 방 모두 조회
    public List<MatchGroupEntity> getAllPublicMatchGroups() {
        return matchGroupRepository.findByIsPublicTrue();
    }

    // 특정 방 조회
    public Optional<MatchGroupEntity> getMatchGroupById(Long groupId) {
        return matchGroupRepository.findById(groupId);
    }
}
