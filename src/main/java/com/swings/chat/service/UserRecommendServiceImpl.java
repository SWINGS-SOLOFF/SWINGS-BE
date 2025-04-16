package com.swings.chat.service;

import com.swings.chat.dto.UserSelectDTO;
import com.swings.chat.repository.UserSelectRepository;
import com.swings.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserRecommendServiceImpl implements UserRecommendService {

    private final UserSelectRepository userSelectRepository;

    @Override
    @Transactional(readOnly = true)
    public UserSelectDTO getRandomUser(String username) {
        // 1. 유저 조회
        UserEntity currentUser = userSelectRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 리스트들 null 방지 처리
        List<String> likedUsernames = Optional.ofNullable(userSelectRepository.findLikedUsernames(username)).orElse(List.of());
        List<String> dislikedUsernames = Optional.ofNullable(userSelectRepository.findDislikedUsernames(username)).orElse(List.of());
        List<String> chatUsernames = Optional.ofNullable(userSelectRepository.findChatUsernames(username)).orElse(List.of());

        // 3. Set으로 중복 제거 + 자기 자신 제외
        Set<String> excludedUsernames = new HashSet<>();
        excludedUsernames.addAll(likedUsernames);
        excludedUsernames.addAll(dislikedUsernames);
        excludedUsernames.addAll(chatUsernames);
        excludedUsernames.add(username);

        // ✅ 로그 출력
        System.out.println("🔥 로그인 유저: " + username);
        System.out.println("🔥 제외 리스트: " + excludedUsernames);

        Optional<UserEntity> recommendedUser;

        // 4. 추천 로직 분기
        if (excludedUsernames.isEmpty()) {
            recommendedUser = userSelectRepository.findRandomUser(currentUser.getGender().name());
        } else {
            recommendedUser = userSelectRepository.findFilteredRandomUser(
                    currentUser.getGender().name(),
                    new ArrayList<>(excludedUsernames)
            );

        }

        // 5. NPE 방어하며 DTO 생성
        return recommendedUser.map(user -> new UserSelectDTO(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getGender() != null ? user.getGender().name() : "UNKNOWN",
                user.getUserImg(),
                user.getIntroduce(),
                user.getActivityRegion() != null ? user.getActivityRegion().name() : "UNKNOWN",
                user.getUserImg() // ✅ 이게 targetUserImg로 들어감
        )).orElseThrow(() -> new RuntimeException("추천할 사용자가 없습니다."));

    }

    @Override
    @Transactional(readOnly = true)
    public UserSelectDTO getNextRandomUser(String username, String excludedUsername) {
        return getRandomUser(username);
    }
}
