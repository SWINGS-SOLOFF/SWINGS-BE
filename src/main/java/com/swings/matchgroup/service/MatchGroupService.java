package com.swings.matchgroup.service;

import com.swings.matchgroup.DTO.MatchGroupDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchGroupService {

    private final MatchGroupRepository matchGroupRepository;
    
    // 방 생성
    public MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO) {
        MatchGroupEntity matchGroup = matchGroupRepository.save(toEntity(matchGroupDTO));
        return toDTO(matchGroup);
    }
    
    // 모든 방 조회
    public List<MatchGroupDTO> getAllPublicMatchGroups() {
        return matchGroupRepository.findByIsPublicTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 특정 방 조회
    public MatchGroupDTO getMatchGroupById(Long groupId) {
        MatchGroupEntity groupEntity = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 방을 찾을 수 없습니다."));
        return toDTO(groupEntity);
    }

    // Entity → DTO 변환 메서드
    private MatchGroupDTO toDTO(MatchGroupEntity entity) {
        return MatchGroupDTO.builder()
                .matchGroupId(entity.getMatchGroupId())
                .username(entity.getUsername())
                .location(entity.getLocation())
                .schedule(entity.getSchedule())
                .playStyle(entity.getPlayStyle())
                .genderRatio(entity.getGenderRatio())
                .skillLevel(entity.getSkillLevel())
                .ageRange(entity.getAgeRange())
                .additionalOptions(entity.getAdditionalOptions())
                .isPublic(entity.getIsPublic())
                .maxParticipants(entity.getMaxParticipants())
                .build();
    }

    // DTO → Entity 변환 메서드
    private MatchGroupEntity toEntity(MatchGroupDTO dto) {
        return MatchGroupEntity.builder()
                .matchGroupId(dto.getMatchGroupId())
                .username(dto.getUsername())
                .location(dto.getLocation())
                .schedule(dto.getSchedule())
                .playStyle(dto.getPlayStyle())
                .genderRatio(dto.getGenderRatio())
                .skillLevel(dto.getSkillLevel())
                .ageRange(dto.getAgeRange())
                .additionalOptions(dto.getAdditionalOptions())
                .isPublic(dto.getIsPublic())
                .maxParticipants(dto.getMaxParticipants())
                .build();
    }
}