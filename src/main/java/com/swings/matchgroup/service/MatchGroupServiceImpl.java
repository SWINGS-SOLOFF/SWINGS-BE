    package com.swings.matchgroup.service;

    import com.swings.matchgroup.dto.MatchGroupDTO;
    import com.swings.matchgroup.dto.MatchGroupNearbyProjection;
    import com.swings.matchgroup.dto.MatchParticipantDTO;
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

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다."));

            int total = matchGroupDTO.getFemaleLimit() + matchGroupDTO.getMaleLimit();
            if (total != matchGroupDTO.getMaxParticipants()) {
                throw new IllegalArgumentException("성비 인원 수 합이 최대 인원과 일치하지 않습니다.");
            }

            MatchGroupEntity matchGroup = matchGroupDTO.toEntity(user);
            MatchGroupEntity saved = matchGroupRepository.save(matchGroup);
            log.info("그룹 저장 완료, ID: {}", saved.getMatchGroupId());

            MatchParticipantEntity hostParticipant = MatchParticipantEntity.builder()
                    .matchGroup(saved)
                    .user(user)
                    .participantStatus(MatchParticipantEntity.ParticipantStatus.ACCEPTED)
                    .joinAt(LocalDateTime.now())
                    .build();
            matchParticipantRepository.save(hostParticipant);
            log.info("방장 자동 참가 처리 완료");

            // 참가자 목록 포함 반환
            List<MatchParticipantDTO> participantDTOs = matchParticipantRepository
                    .findByMatchGroupMatchGroupId(saved.getMatchGroupId()).stream()
                    .map(MatchParticipantDTO::fromEntity)
                    .collect(Collectors.toList());

            return MatchGroupDTO.fromEntity(saved, participantDTOs);
        }

        // 2. 전체 그룹 목록 조회
        @Override
        public List<MatchGroupDTO> getAllMatchGroups() {
            log.info("전체 방 목록 조회 요청 실행");

            List<MatchGroupEntity> groups = matchGroupRepository.findAllWithHost();
            log.info("조회된 전체 그룹 개수: {}", groups.size());

            return groups.stream()
                    .map(group -> {
                        List<MatchParticipantDTO> participantDTOs = matchParticipantRepository
                                .findByMatchGroupMatchGroupId(group.getMatchGroupId()).stream()
                                .map(MatchParticipantDTO::fromEntity)
                                .collect(Collectors.toList());

                        return MatchGroupDTO.fromEntity(group, participantDTOs);
                    })
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

            List<MatchParticipantDTO> participantDTOs = matchParticipantRepository
                    .findByMatchGroupMatchGroupId(groupId).stream()
                    .map(MatchParticipantDTO::fromEntity)
                    .collect(Collectors.toList());

            return MatchGroupDTO.fromEntity(groupEntity, participantDTOs);
        }

        // 5. 내가 방장인 그룹 찾기
        @Override
        public List<MatchGroupDTO> getGroupsByHost(Long hostId) {
            log.info("방장 ID {}가 생성한 그룹 목록 조회", hostId);

            List<MatchGroupEntity> groups = matchGroupRepository.findByHostUserId(hostId);

            return groups.stream()
                    .map(group -> {
                        List<MatchParticipantDTO> participantDTOs = matchParticipantRepository
                                .findByMatchGroupMatchGroupId(group.getMatchGroupId()).stream()
                                .map(MatchParticipantDTO::fromEntity)
                                .collect(Collectors.toList());

                        return MatchGroupDTO.fromEntity(group, participantDTOs);
                    })
                    .collect(Collectors.toList());
        }


        // 6. 근처 그룹 찾기
        @Override
        public List<MatchGroupDTO> findNearbyGroups(double latitude, double longitude, double radiusInKm) {
            List<MatchGroupNearbyProjection> results = matchGroupRepository
                    .findNearbyGroupsProjected(latitude, longitude, radiusInKm);

            return results.stream()
                    .map(MatchGroupDTO::fromProjection)
                    .collect(Collectors.toList());
        }
    }