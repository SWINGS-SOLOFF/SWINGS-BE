package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 🔥 아이디 중복 확인 메서드 (회원가입과 별도로 사용)
     */
    public Map<String, Boolean> checkUsername(String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userRepository.findByUsername(username).isPresent());
        return response;
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
    }
}

