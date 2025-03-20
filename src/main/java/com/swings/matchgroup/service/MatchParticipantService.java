package com.swings.matchgroup.service;

import com.swings.matchgroup.DTO.MatchParticipantDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import com.swings.matchgroup.repository.MatchParticipantRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;
    private final MatchGroupRepository matchGroupRepository;
    private final UserRepository userRepository;

    // 참가 신청
    public MatchParticipantDTO joinMatch(Long groupId, String username){
        MatchGroupEntity matchGroup = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        // username 이용 UserEntity 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 중복 참가 방지
        boolean alreadyJoined = matchParticipantRepository.existsByMatchGroup_MatchGroupIdAndUser_UserId(groupId, user.getUserId());
        if(alreadyJoined){
            throw new RuntimeException("이미 참가한 사용자입니다.");
        }

        // user 객체 저장
        MatchParticipantEntity participant = MatchParticipantEntity.builder()
                .matchGroup(matchGroup)
                .user(user)
                .participantStatus(MatchParticipantEntity.ParticipantStatus.PENDING)
                .joinAt(LocalDateTime.now())
                .build();

        MatchParticipantEntity savedParticipant = matchParticipantRepository.save(participant);
        return toDTO(savedParticipant);
    }

    // 특정 방의 참가자 목록 조회
    public List<MatchParticipantDTO> getParticipantsByGroupId(Long groupId) {
        MatchGroupEntity matchGroup = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        return matchParticipantRepository.findByMatchGroup(matchGroup)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Entity → DTO 변환 메서드
    private MatchParticipantDTO toDTO(MatchParticipantEntity entity) {
        return MatchParticipantDTO.builder()
                .matchParticipantId(entity.getMatchParticipantId())
                .matchGroupId(entity.getMatchGroup().getMatchGroupId())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
