package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchGroupDTO;
import com.swings.matchgroup.dto.MatchGroupNearbyProjection;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import com.swings.matchgroup.repository.MatchParticipantRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchGroupServiceImpl implements MatchGroupService {

    private final MatchGroupRepository matchGroupRepository;
    private final UserRepository userRepository;
    private final MatchParticipantRepository matchParticipantRepository;

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
        MatchGroupEntity matchGroup = matchGroupDTO.toEntity(user);

        // 저장
        MatchGroupEntity saved = matchGroupRepository.save(matchGroup);
        log.info("그룹 저장 완료, ID: {}", saved.getMatchGroupId());

        // 방장은 자동 참가 처리
        MatchParticipantEntity hostParticipant = MatchParticipantEntity.builder()
                .matchGroup(saved)
                .user(user)
                .participantStatus(MatchParticipantEntity.ParticipantStatus.ACCEPTED)
                .joinAt(LocalDateTime.now())
                .build();
        matchParticipantRepository.save(hostParticipant);

        log.info("그룹 저장 및 방장 참가자 등록 완료, ID: {}", saved.getMatchGroupId());

        // Entity → DTO
        return MatchGroupDTO.fromEntity(saved);
    }

    // 2. 전체 그룹 목록 조회
    @Override
    public List<MatchGroupDTO> getAllMatchGroups() {
        log.info("전체 방 목록 조회 요청 실행");

        List<MatchGroupEntity> groups = matchGroupRepository.findAllWithHost();
        log.info("조회된 전체 그룹 개수: {}", groups.size());

        return groups.stream()
                .map(MatchGroupDTO::fromEntity)
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

        return MatchGroupDTO.fromEntity(groupEntity);
    }

    // 5. 근처 그룹 찾기
    @Override
    public List<MatchGroupDTO> findNearbyGroups(double latitude, double longitude, double radiusInKm) {
        // Projection 결과 가져오기
        List<MatchGroupNearbyProjection> results = matchGroupRepository.findNearbyGroupsProjected(latitude, longitude, radiusInKm);

        // Projection → DTO
        return results.stream()
                .map(MatchGroupDTO::fromProjection)
                .collect(Collectors.toList());
    }

}
