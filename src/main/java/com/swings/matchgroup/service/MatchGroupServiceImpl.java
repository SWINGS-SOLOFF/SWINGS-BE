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

    // 그룹 생성
    @Override
    public MatchGroupDTO createMatchGroup(MatchGroupDTO matchGroupDTO) {
        log.info("그룹 생성 요청: {}", matchGroupDTO);

        // 현재 로그인한 사용자 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다."));

        // Entity로 변환 후 host 설정
        MatchGroupEntity matchGroup = toEntity(matchGroupDTO);
        matchGroup.setHost(user); // 방장 저장

        MatchGroupEntity saved = matchGroupRepository.save(matchGroup);
        return toDTO(saved); // 저장 후 DTO 반환
    }
    
    // 그룹 모두 보기
    @Override
    public List<MatchGroupDTO> getAllMatchGroups() {
        log.info("전체 방 목록 조회 요청 실행");
        List<MatchGroupEntity> groups = matchGroupRepository.findAll();
        log.info("조회된 전체 그룹 개수: {}", groups.size());

        return groups.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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


    // DTO를 Entity로 변환
    private MatchGroupEntity toEntity(MatchGroupDTO dto) {
        return MatchGroupEntity.builder()
                .matchGroupId(dto.getMatchGroupId())
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


