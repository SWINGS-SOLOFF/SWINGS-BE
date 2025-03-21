package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;
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
public class MatchParticipantServiceImpl implements MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;
    private final MatchGroupRepository matchGroupRepository;
    private final UserRepository userRepository;

    @Override
    public MatchParticipantDTO joinMatch(Long groupId, String username) {
        MatchGroupEntity matchGroup = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 중복 참가 방지
        boolean alreadyJoined = matchParticipantRepository.existsByMatchGroup_MatchGroupIdAndUser_UserId(groupId, user.getUserId());
        if (alreadyJoined) {
            throw new RuntimeException("이미 참가한 사용자입니다.");
        }

        MatchParticipantEntity participant = MatchParticipantEntity.builder()
                .matchGroup(matchGroup)
                .user(user)
                .participantStatus(MatchParticipantEntity.ParticipantStatus.PENDING)
                .joinAt(LocalDateTime.now())
                .build();

        return MatchParticipantDTO.fromEntity(matchParticipantRepository.save(participant));
    }

    @Override
    public List<MatchParticipantDTO> getParticipantsByGroupId(Long groupId) {
        MatchGroupEntity matchGroup = matchGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        return matchParticipantRepository.findByMatchGroup(matchGroup)
                .stream()
                .map(MatchParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
