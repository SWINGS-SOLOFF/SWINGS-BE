package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import com.swings.matchgroup.repository.MatchParticipantRepository;
import com.swings.notification.service.NotificationServiceImpl;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final NotificationServiceImpl notificationService;

    // 참가 신청
    @Override
    @Transactional
    public MatchParticipantDTO joinMatch(Long matchGroupId, String username) {
        MatchGroupEntity matchGroup = matchGroupRepository.findById(matchGroupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 중복 신청 방지
        boolean alreadyJoined = matchParticipantRepository.existsByMatchGroup_MatchGroupIdAndUser_UserId(
                matchGroupId, user.getUserId());
        if (alreadyJoined) {
            throw new RuntimeException("이미 참가한 사용자입니다.");
        }

        MatchParticipantEntity participant = MatchParticipantEntity.builder()
                .matchGroup(matchGroup)
                .user(user)
                .participantStatus(MatchParticipantEntity.ParticipantStatus.PENDING)
                .joinAt(LocalDateTime.now())
                .build();

        // 알림 기능
        notificationService.notifyHostOnJoinRequest(
                matchGroup.getGroupName(),
                matchGroup.getHost().getUsername(),
                user.getUsername()
        );

        return MatchParticipantDTO.fromEntity(matchParticipantRepository.save(participant));
    }

    // 참가 신청 승인(방장)
    @Override
    @Transactional
    public void approveParticipant(Long matchGroupId, Long matchParticipantId, String hostUsername) {
        MatchParticipantEntity participant = matchParticipantRepository.findById(matchParticipantId)
                .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        MatchGroupEntity matchGroup = participant.getMatchGroup();

        if (!matchGroup.getMatchGroupId().equals(matchGroupId)) {
            throw new RuntimeException("그룹 ID가 일치하지 않습니다.");
        }

        if (!matchGroup.getHost().getUsername().equals(hostUsername)) {
            throw new RuntimeException("방장만 참가자를 승인할 수 있습니다.");
        }

        participant.setParticipantStatus(MatchParticipantEntity.ParticipantStatus.ACCEPTED);
        matchParticipantRepository.save(participant);

        // 알림 기능
        notificationService.notifyUserOnApproval(
                matchGroup.getGroupName(),
                participant.getUser().getUsername()
        );
    }

    // 참가 신청 거절(방장)
    @Override
    @Transactional
    public void rejectParticipant(Long matchGroupId, Long matchParticipantId, String hostUsername) {
        MatchParticipantEntity participant = matchParticipantRepository.findById(matchParticipantId)
                .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        MatchGroupEntity matchGroup = participant.getMatchGroup();

        if (!matchGroup.getMatchGroupId().equals(matchGroupId)) {
            throw new RuntimeException("그룹 ID가 일치하지 않습니다.");
        }

        if (!matchGroup.getHost().getUsername().equals(hostUsername)) {
            throw new RuntimeException("방장만 참가자를 거절할 수 있습니다.");
        }

        participant.setParticipantStatus(MatchParticipantEntity.ParticipantStatus.REJECTED);
        matchParticipantRepository.save(participant);

        // 알림 기능
        notificationService.notifyUserOnRejection(
                matchGroup.getGroupName(),
                participant.getUser().getUsername()
        );
    }

    // 특정 방의 참가 신청자 목록 조회
    @Override
    public List<MatchParticipantDTO> getParticipantsByMatchGroupId(Long matchGroupId) {
        return matchParticipantRepository.findByMatchGroupMatchGroupId(matchGroupId)
                .stream()
                .map(MatchParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 방의 참가자 목록 조회
    @Override
    public List<MatchParticipantDTO> getAcceptedParticipants(Long matchGroupId) {
        return matchParticipantRepository.findByMatchGroupMatchGroupIdAndParticipantStatus(
                        matchGroupId,
                        MatchParticipantEntity.ParticipantStatus.ACCEPTED
                ).stream()
                .map(MatchParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
