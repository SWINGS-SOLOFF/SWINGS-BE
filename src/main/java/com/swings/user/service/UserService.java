package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .golfSkill(UserEntity.GolfSkill.fromString(dto.getGolfSkill())) // 🔹 String → Enum 변환
                .mbti(dto.getMbti())
                .hobbies(dto.getHobbies())
                .religion(dto.getReligion())
                .smoking(UserEntity.YesNo.fromString(dto.getSmoking())) // 🔹 String → Enum 변환
                .drinking(UserEntity.YesNo.fromString(dto.getDrinking())) // 🔹 String → Enum 변환
                .introduce(dto.getIntroduce())
                .userImg(dto.getUserImg()) // Base64 이미지 저장 (URL 변환 불필요)
                .role(UserEntity.Role.fromString(dto.getRole())) // 🔹 String → Enum 변환
                .gender(UserEntity.Gender.fromString(dto.getGender())) // 🔹 String → Enum 변환
                .build();

        return userRepository.save(user);
    }
}

