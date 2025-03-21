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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity registerUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(encryptedPassword)
                .name(dto.getName())
                .gender(UserEntity.Gender.fromString(dto.getGender()))
                .phone_number(dto.getPhone_number())
                .job(dto.getJob())
                .golf_skill(UserEntity.GolfSkill.fromString(dto.getGolf_skill()))
                .mbti(dto.getMbti())
                .hobbies(dto.getHobbies())
                .religion(dto.getReligion())
                .smoking(UserEntity.YesNo.fromString(dto.getSmoking()))
                .drinking(UserEntity.YesNo.fromString(dto.getDrinking()))
                .introduce(dto.getIntroduce())
                .user_img(dto.getUser_img())
                .role(UserEntity.Role.fromString(dto.getRole()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return getUserByUsername(username);
        } else {
            throw new IllegalArgumentException("인증 정보가 유효하지 않습니다.");
        }
    }

    @Override
    public UserEntity updateUser(String username, UserDTO dto) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }
        if (dto.getPhone_number() != null) user.setPhone_number(dto.getPhone_number());
        if (dto.getJob() != null) user.setJob(dto.getJob());
        if (dto.getGolf_skill() != null) user.setGolf_skill(UserEntity.GolfSkill.fromString(dto.getGolf_skill()));
        if (dto.getMbti() != null) user.setMbti(dto.getMbti());
        if (dto.getHobbies() != null) user.setHobbies(dto.getHobbies());
        if (dto.getReligion() != null) user.setReligion(dto.getReligion());
        if (dto.getSmoking() != null) user.setSmoking(UserEntity.YesNo.fromString(dto.getSmoking()));
        if (dto.getDrinking() != null) user.setDrinking(UserEntity.YesNo.fromString(dto.getDrinking()));
        if (dto.getIntroduce() != null) user.setIntroduce(dto.getIntroduce());
        if (dto.getUser_img() != null) user.setUser_img(dto.getUser_img());
        if (dto.getRole() != null) user.setRole(UserEntity.Role.fromString(dto.getRole()));
        if (dto.getGender() != null) user.setGender(UserEntity.Gender.fromString(dto.getGender()));

        return userRepository.save(user);
    }
}
