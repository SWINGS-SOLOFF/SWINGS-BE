package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchGroupDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchGroupServiceImpl implements MatchGroupService {

    private final MatchGroupRepository matchGroupRepository;
    private final UserRepository userRepository;

    // 1. 그룹 생성
    @Override
    public MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO) {
        log.info("그룹 생성 요청: {}", matchGroupDTO);

        // 현재 로그인한 사용자 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다."));

        // DTO → Entity 변환 + host 지정
        MatchGroupEntity matchGroup = toEntity(matchGroupDTO, user);

        // 저장
        MatchGroupEntity saved = matchGroupRepository.save(matchGroup);
        log.info("그룹 저장 완료, ID: {}", saved.getMatchGroupId());

        return toDTO(saved);
    }

    // 2. 전체 그룹 목록 조회
    @Override
    public List<MatchGroupDTO> getAllMatchGroups() {
        log.info("전체 방 목록 조회 요청 실행");

        List<MatchGroupEntity> groups = matchGroupRepository.findAllWithHost();
        log.info("조회된 전체 그룹 개수: {}", groups.size());

        return groups.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 3. 그룹 상세 조회 (by ID)
    @Override
    public MatchGroupDTO getMatchGroupById(Long groupId) {
        log.info("ID {}의 방 조회 요청", groupId);

        MatchGroupEntity groupEntity = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> {
                    log.warn("방 ID {}를 찾을 수 없음", groupId);
                    return new RuntimeException("해당 방을 찾을 수 없습니다.");
                });

        return toDTO(groupEntity);
    }

    // Entity → DTO 변환
    private MatchGroupDTO toDTO(MatchGroupEntity entity) {
        return MatchGroupDTO.builder()
                .matchGroupId(entity.getMatchGroupId())
                .hostId(entity.getHost().getUserId())
                .hostUsername(entity.getHost().getUsername())
                .groupName(entity.getGroupName())
                .location(entity.getLocation())
                .schedule(entity.getSchedule())
                .playStyle(entity.getPlayStyle())
                .genderRatio(entity.getGenderRatio())
                .skillLevel(entity.getSkillLevel())
                .ageRange(entity.getAgeRange())
                .description(entity.getDescription())
                .matchType(entity.getMatchType())
                .maxParticipants(entity.getMaxParticipants())
                .build();
    }

    // DTO → Entity 변환
    private MatchGroupEntity toEntity(MatchGroupDTO dto, UserEntity host) {
        return MatchGroupEntity.builder()
                .host(host)
                .groupName(dto.getGroupName())
                .location(dto.getLocation())
                .schedule(dto.getSchedule())
                .playStyle(dto.getPlayStyle())
                .genderRatio(dto.getGenderRatio())
                .skillLevel(dto.getSkillLevel())
                .ageRange(dto.getAgeRange())
                .description(dto.getDescription())
                .matchType(dto.getMatchType())
                .maxParticipants(dto.getMaxParticipants())
                .build();
    }
}