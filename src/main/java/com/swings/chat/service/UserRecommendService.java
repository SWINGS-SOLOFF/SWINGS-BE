package com.swings.chat.service;

import com.swings.chat.dto.UserSelectDTO;
import com.swings.user.entity.UserEntity;
import com.swings.chat.repository.UserSelectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRecommendService {
    private final UserSelectRepository userSelectRepository;

    // 기존 추천 기능 (한 명 추천)
    @Transactional(readOnly = true)
    public UserSelectDTO getRandomUser(String username) {
        UserEntity currentUser = userSelectRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Optional<UserEntity> recommendedUser = userSelectRepository.findRandomUser(currentUser.getGender());

        return recommendedUser.map(user -> new UserSelectDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getName(),
                        user.getGender().name(),
                        user.getUserImg(),
                        user.getIntroduce()
                ))
                .orElseThrow(() -> new RuntimeException("추천할 사용자가 없습니다."));
    }

    // 싫어요 후 새로운 유저 추천하는 기능
    @Transactional(readOnly = true)
    public UserSelectDTO getNextRandomUser(String username, String excludedUsername) {
        // 현재 사용자 정보 가져오기
        UserEntity currentUser = userSelectRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 특정 유저(excludedUsername)를 제외하고 무작위 추천 유저 조회
        Optional<UserEntity> recommendedUser = userSelectRepository.findNextRandomUser(
                currentUser.getGender(), excludedUsername);

        return recommendedUser.map(user -> new UserSelectDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getName(),
                        user.getGender().name(),
                        user.getUserImg(),
                        user.getIntroduce()
                ))
                .orElseThrow(() -> new RuntimeException("추천할 사용자가 없습니다."));
    }
}
