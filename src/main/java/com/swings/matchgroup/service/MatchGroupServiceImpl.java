package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchGroupDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchGroupServiceImpl implements MatchGroupService {

    private final MatchGroupRepository matchGroupRepository;

    // 그룹 생성
    @Override
    public MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO) {
        log.info("그룹 생성 요청: {}", matchGroupDTO);
        MatchGroupEntity matchGroup = matchGroupRepository.save(toEntity(matchGroupDTO));  // DTO를 Entity로 변환 후 저장
        return toDTO(matchGroup); // 저장된 Entity를 DTO로 변환하여 반환
    }
    
    // 공개 그룹 모두 보기
    @Override
    public List<MatchGroupDTO> getAllPublicMatchGroups() {
        log.info("공개된 방 목록 조회 요청");
        return matchGroupRepository.findByIsPublicTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());  // 공개된 방만 리스트로 변환 후 반환
    }

    // 그룹 찾기 By Id
    @Override
    public MatchGroupDTO getMatchGroupById(Long groupId) {
        log.info("ID {}의 방 조회 요청", groupId);
        MatchGroupEntity groupEntity = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> {
                    log.warn("방 ID {}를 찾을 수 없음", groupId);
                    return new RuntimeException("해당 방을 찾을 수 없습니다.");
                });  // 방이 없을 경우 예외 발생
        return toDTO(groupEntity);
    }


    // Entity를 DTO로 변환
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

    // DTO를 Entity로 변환
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


