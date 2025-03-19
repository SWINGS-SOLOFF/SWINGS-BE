package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 🔥 아이디 중복 확인
     */
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * 🔥 회원가입 (중복 확인은 프론트에서 미리 수행)
     */
    public UserEntity registerUser(UserDTO dto) {
        // 🔥 회원가입 전에 프론트엔드에서 중복 확인 API 호출하도록 유도
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // UserEntity 생성 및 저장
        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(encryptedPassword)
                .name(dto.getName())
                .phonenumber(dto.getPhonenumber())
                .job(dto.getJob())
                .golfSkill(dto.getGolfSkill())
                .mbti(dto.getMbti())
                .hobbies(dto.getHobbies())
                .religion(dto.getReligion())
                .smoking(dto.getSmoking())
                .drinking(dto.getDrinking())
                .introduce(dto.getIntroduce())
                .userImg(dto.getUserImg())
                .role(dto.getRole())
                .gender(dto.getGender())
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .build();

        return userRepository.save(user);

 
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

