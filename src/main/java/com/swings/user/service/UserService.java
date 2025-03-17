package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 (비밀번호 암호화 후 저장)
     */
    public UserEntity registerUser(UserDTO dto) {
        // 아이디 중복 확인
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
                .createdAt(LocalDateTime.now()) // 현재 시간 설정
                .build();

        return userRepository.save(user);



    }
}
