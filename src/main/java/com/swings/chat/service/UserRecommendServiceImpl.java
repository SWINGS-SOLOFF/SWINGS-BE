package com.swings.chat.service;

import com.swings.chat.dto.UserSelectDTO;
import com.swings.chat.repository.UserSelectRepository;
import com.swings.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRecommendServiceImpl implements UserRecommendService {

    private final UserSelectRepository userSelectRepository;

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public UserSelectDTO getNextRandomUser(String username, String excludedUsername) {
        UserEntity currentUser = userSelectRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

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
