package com.swings.matchgroup.service;

import com.swings.matchgroup.dto.MatchParticipantDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.entity.MatchParticipantEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import com.swings.matchgroup.repository.MatchParticipantRepository;
import com.swings.notification.service.FCMService;
import com.swings.notification.service.NotificationServiceImpl;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchParticipantServiceImpl implements MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;
    private final MatchGroupRepository matchGroupRepository;
    private final UserRepository userRepository;
    private final NotificationServiceImpl notificationService;
    private final FCMService fcmService;

    // 공통 유저 정보 세팅
    private void enrichUserInfo(MatchParticipantDTO dto) {
        userRepository.findById(dto.getUserId()).ifPresent(user -> {
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setMbti(user.getMbti());
            dto.setJob(user.getJob());
            dto.setUserImg(user.getUserImg());
            dto.setGender(user.getGender().name());
            dto.setRegion(user.getActivityRegion().name());
            int currentYear = LocalDate.now().getYear();
            dto.setAge(currentYear - user.getBirthDate().getYear() + 1);
        });
    }

    // 참가 신청
    @Override
    @Transactional
    public MatchParticipantDTO joinMatch(Long matchGroupId, Long userId) {
        MatchGroupEntity matchGroup = matchGroupRepository.findById(matchGroupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        boolean alreadyJoined = matchParticipantRepository
                .existsByMatchGroup_MatchGroupIdAndUser_UserId(matchGroupId, userId);
        if (alreadyJoined) {
            throw new RuntimeException("이미 참가한 사용자입니다.");
        }

        MatchParticipantEntity participant = MatchParticipantEntity.builder()
                .matchGroup(matchGroup)
                .user(user)
                .participantStatus(MatchParticipantEntity.ParticipantStatus.PENDING)
                .joinAt(LocalDateTime.now())
                .build();

        MatchParticipantEntity saved = matchParticipantRepository.save(participant);

        // 방장이 아닐 때만 알림 전송
        if (!user.getUserId().equals(matchGroup.getHost().getUserId())) {
            notificationService.notifyHostOnJoinRequest(
                    matchGroup.getGroupName(),
                    matchGroup.getHost().getUsername(),
                    user.getUsername()
            );

            if (matchGroup.getHost().getPushToken() != null) {
                fcmService.sendPush(
                        matchGroup.getHost().getPushToken(),
                        "⛳ 참가 신청 알림",
                        user.getUsername() + "님이 [" + matchGroup.getGroupName() + "]에 참가 신청했습니다."
                );
            }
        }

        MatchParticipantDTO dto = MatchParticipantDTO.fromEntity(saved);
        enrichUserInfo(dto);
        return dto;
    }

    // 참가 신청 취소
    @Override
    @Transactional
    public void leaveMatch(Long matchGroupId, Long userId) {
        List<MatchParticipantEntity> participants = matchParticipantRepository
                .findByMatchGroupMatchGroupId(matchGroupId);

        MatchParticipantEntity participant = participants.stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("참가 정보를 찾을 수 없습니다."));

        matchParticipantRepository.delete(participant);
    }

    // 참가 승인
    @Override
    @Transactional
    public void approveParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId) {
        MatchParticipantEntity participant = matchParticipantRepository.findById(matchParticipantId)
                .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        MatchGroupEntity matchGroup = participant.getMatchGroup();

        if (!matchGroup.getMatchGroupId().equals(matchGroupId)) {
            throw new RuntimeException("그룹 ID가 일치하지 않습니다.");
        }

        if (!matchGroup.getHost().getUserId().equals(hostUserId)) {
            throw new RuntimeException("방장만 참가자를 승인할 수 있습니다.");
        }

        participant.setParticipantStatus(MatchParticipantEntity.ParticipantStatus.ACCEPTED);
        matchParticipantRepository.save(participant);

        notificationService.notifyUserOnApproval(
                matchGroup.getGroupName(),
                participant.getUser().getUsername()
        );

        UserEntity target = participant.getUser();
        if (target.getPushToken() != null) {
            fcmService.sendPush(
                    target.getPushToken(),
                    "🎉 참가 승인 완료",
                    "[" + matchGroup.getGroupName() + "] 참가가 승인되었습니다."
            );
        }
    }

    // 참가 거절
    @Override
    @Transactional
    public void rejectParticipant(Long matchGroupId, Long matchParticipantId, Long hostUserId) {
        MatchParticipantEntity participant = matchParticipantRepository.findById(matchParticipantId)
                .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        MatchGroupEntity matchGroup = participant.getMatchGroup();

        if (!matchGroup.getMatchGroupId().equals(matchGroupId)) {
            throw new RuntimeException("그룹 ID가 일치하지 않습니다.");
        }

        if (!matchGroup.getHost().getUserId().equals(hostUserId)) {
            throw new RuntimeException("방장만 참가자를 거절할 수 있습니다.");
        }

        participant.setParticipantStatus(MatchParticipantEntity.ParticipantStatus.REJECTED);
        matchParticipantRepository.save(participant);

        notificationService.notifyUserOnRejection(
                matchGroup.getGroupName(),
                participant.getUser().getUsername()
        );

        UserEntity target = participant.getUser();
        if (target.getPushToken() != null) {
            fcmService.sendPush(
                    target.getPushToken(),
                    "❌ 참가 거절 안내",
                    "[" + matchGroup.getGroupName() + "] 참가가 거절되었습니다."
            );
        }
    }

    // 강퇴
    @Override
    @Transactional
    public void removeParticipant(Long matchGroupId, Long userId, Long hostUserId) {
        MatchGroupEntity group = matchGroupRepository.findById(matchGroupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        if (!group.getHost().getUserId().equals(hostUserId)) {
            throw new RuntimeException("방장만 강퇴할 수 있습니다.");
        }

        MatchParticipantEntity participant = matchParticipantRepository.findByMatchGroupMatchGroupId(matchGroupId)
                .stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("참가자를 찾을 수 없습니다."));

        matchParticipantRepository.delete(participant);
    }

    // 참가자 목록 조회
    @Override
    public List<MatchParticipantDTO> getParticipantsByMatchGroupId(Long matchGroupId) {
        return matchParticipantRepository.findByMatchGroupMatchGroupId(matchGroupId)
                .stream()
                .map(entity -> {
                    MatchParticipantDTO dto = MatchParticipantDTO.fromEntity(entity);
                    enrichUserInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 승인된 참가자 목록 조회
    @Override
    public List<MatchParticipantDTO> getAcceptedParticipants(Long matchGroupId) {
        return matchParticipantRepository.findByMatchGroupMatchGroupIdAndParticipantStatus(
                        matchGroupId,
                        MatchParticipantEntity.ParticipantStatus.ACCEPTED
                ).stream()
                .map(entity -> {
                    MatchParticipantDTO dto = MatchParticipantDTO.fromEntity(entity);
                    enrichUserInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 나의 참가 그룹 조회
    @Override
    public List<MatchParticipantDTO> getMyGroups(MatchParticipantDTO request) {
        Long userId = request.getUserId();
        String status = request.getParticipantStatus();

        if (userId == null) {
            throw new RuntimeException("userId가 필요합니다.");
        }

        List<MatchParticipantEntity> result;

        if (status != null && !status.isEmpty()) {
            MatchParticipantEntity.ParticipantStatus enumStatus =
                    MatchParticipantEntity.ParticipantStatus.valueOf(status.toUpperCase());
            result = matchParticipantRepository.findByUser_UserIdAndParticipantStatus(userId, enumStatus);
        } else {
            result = matchParticipantRepository.findByUser_UserId(userId);
        }

        return result.stream()
                .map(entity -> {
                    MatchParticipantDTO dto = MatchParticipantDTO.fromEntity(entity);
                    enrichUserInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}