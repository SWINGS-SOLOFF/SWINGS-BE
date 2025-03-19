package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 기존 추천 기능 (한 명 추천)
    @Transactional(readOnly = true)
    public UserDTO getRandomUser(String username) {
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Optional<UserEntity> recommendedUser = userRepository.findRandomUser(currentUser.getGender());

        return recommendedUser.map(UserDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("추천할 사용자가 없습니다."));
    }

    // 싫어요 후 새로운 유저 추천하는 기능
    @Transactional(readOnly = true)
    public UserDTO getNextRandomUser(String username, String excludedUsername) {
        // 현재 사용자 정보 가져오기
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 특정 유저(excludedUsername)를 제외하고 무작위 추천 유저 조회
        Optional<UserEntity> recommendedUser = userRepository.findNextRandomUser(
                currentUser.getGender(), excludedUsername);

        return recommendedUser.map(UserDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("추천할 사용자가 없습니다."));
    }
}
