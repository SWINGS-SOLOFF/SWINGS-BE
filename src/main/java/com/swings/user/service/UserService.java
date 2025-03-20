package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디 중복 확인
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // 회원가입
    public UserEntity registerUser(UserDTO dto) {
        // 회원가입 전에 프론트엔드에서 중복 확인 API 호출하도록 유도
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO -> Entity 변환
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

    // 🔹 특정 username으로 사용자 조회
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    // 🔹 현재 로그인한 사용자 조회
    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return getUserByUsername(username);
        } else {
            throw new IllegalArgumentException("인증 정보가 유효하지 않습니다.");
        }
    }

    //회원 수정
    public UserEntity updateUser(String username, UserDTO dto) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 🔹 이름 변경 요청이 있을 경우에만 변경
        if (dto.getName() != null) user.setName(dto.getName());

        // 🔹 비밀번호 변경 요청이 있을 경우에만 암호화 후 변경
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        // 🔹 전화번호 변경 요청이 있을 경우에만 변경
        if (dto.getPhonenumber() != null) user.setPhonenumber(dto.getPhonenumber());

        // 🔹 직업 변경 요청이 있을 경우에만 변경
        if (dto.getJob() != null) user.setJob(dto.getJob());

        // 🔹 골프 실력 변경 요청이 있을 경우에만 변경
        if (dto.getGolfSkill() != null) user.setGolfSkill(UserEntity.GolfSkill.fromString(dto.getGolfSkill()));

        // 🔹 MBTI 변경 요청이 있을 경우에만 변경
        if (dto.getMbti() != null) user.setMbti(dto.getMbti());

        // 🔹 취미 변경 요청이 있을 경우에만 변경
        if (dto.getHobbies() != null) user.setHobbies(dto.getHobbies());

        // 🔹 종교 변경 요청이 있을 경우에만 변경
        if (dto.getReligion() != null) user.setReligion(dto.getReligion());

        // 🔹 흡연 여부 변경 요청이 있을 경우에만 변경
        if (dto.getSmoking() != null) user.setSmoking(UserEntity.YesNo.fromString(dto.getSmoking()));

        // 🔹 음주 여부 변경 요청이 있을 경우에만 변경
        if (dto.getDrinking() != null) user.setDrinking(UserEntity.YesNo.fromString(dto.getDrinking()));

        // 🔹 자기소개 변경 요청이 있을 경우에만 변경
        if (dto.getIntroduce() != null) user.setIntroduce(dto.getIntroduce());

        // 🔹 프로필 이미지 변경 요청이 있을 경우에만 변경
        if (dto.getUserImg() != null) user.setUserImg(dto.getUserImg());

        // 🔹 역할 변경 요청이 있을 경우에만 변경
        if (dto.getRole() != null) user.setRole(UserEntity.Role.fromString(dto.getRole()));

        // 🔹 성별 변경 요청이 있을 경우에만 변경
        if (dto.getGender() != null) user.setGender(UserEntity.Gender.fromString(dto.getGender()));

        return userRepository.save(user); // 변경된 정보 저장
    }

}