package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                .birthDate(LocalDate.parse(dto.getBirthDate()))
                .email(dto.getEmail())
                .phonenumber(dto.getPhonenumber())
                .job(dto.getJob())
                .golfSkill(UserEntity.GolfSkill.fromString(dto.getGolfSkill()))
                .mbti(dto.getMbti())
                .hobbies(dto.getHobbies())
                .religion(dto.getReligion())
                .smoking(UserEntity.YesNo.fromString(dto.getSmoking()))
                .drinking(UserEntity.YesNo.fromString(dto.getDrinking()))
                .introduce(dto.getIntroduce())
                .userImg(dto.getUserImg())
                .role(UserEntity.Role.fromString(dto.getRole()))
                .activityRegion(UserEntity.ActivityRegion.fromString(dto.getActivityRegion()))
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

    // ✅ 추가된 부분: UserEntity -> UserDTO 변환
    public UserDTO getCurrentUserDto() {
        return convertToDto(getCurrentUser());
    }

    private UserDTO convertToDto(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setPassword(null); // 보안상 null 처리
        dto.setName(user.getName());
        dto.setBirthDate(user.getBirthDate().toString());
        dto.setPhonenumber(user.getPhonenumber());
        dto.setEmail(user.getEmail());
        dto.setJob(user.getJob());
        dto.setGolfSkill(user.getGolfSkill().name());
        dto.setMbti(user.getMbti());
        dto.setHobbies(user.getHobbies());
        dto.setReligion(user.getReligion());
        dto.setSmoking(user.getSmoking().name());
        dto.setDrinking(user.getDrinking().name());
        dto.setIntroduce(user.getIntroduce());
        dto.setUserImg(user.getUserImg());
        dto.setRole(user.getRole().name());
        dto.setGender(user.getGender().name());
        dto.setActivityRegion(user.getActivityRegion().name());
        return dto;
    }

    @Override
    public UserEntity updateUser(String username, UserDTO dto) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            userRepository.findByUsername(dto.getUsername())
                    .ifPresent(u -> {
                        throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
                    });

            user.setUsername(dto.getUsername());
        }

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }
        if (dto.getBirthDate() != null) {
            user.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        }

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhonenumber() != null) user.setPhonenumber(dto.getPhonenumber());
        if (dto.getJob() != null) user.setJob(dto.getJob());
        if (dto.getGolfSkill() != null) user.setGolfSkill(UserEntity.GolfSkill.fromString(dto.getGolfSkill()));
        if (dto.getMbti() != null) user.setMbti(dto.getMbti());
        if (dto.getHobbies() != null) user.setHobbies(dto.getHobbies());
        if (dto.getReligion() != null) user.setReligion(dto.getReligion());
        if (dto.getSmoking() != null) user.setSmoking(UserEntity.YesNo.fromString(dto.getSmoking()));
        if (dto.getDrinking() != null) user.setDrinking(UserEntity.YesNo.fromString(dto.getDrinking()));
        if (dto.getIntroduce() != null) user.setIntroduce(dto.getIntroduce());
        if (dto.getUserImg() != null) user.setUserImg(dto.getUserImg());
        if (dto.getRole() != null) user.setRole(UserEntity.Role.fromString(dto.getRole()));
        if (dto.getGender() != null) user.setGender(UserEntity.Gender.fromString(dto.getGender()));
        if (dto.getActivityRegion() != null) user.setActivityRegion(UserEntity.ActivityRegion.fromString(dto.getActivityRegion()));

        return userRepository.save(user);
    }

    @Override
    public void deleteCurrentUserWithPassword(String password) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("인증 정보가 유효하지 않습니다.");
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserByUsername(String username) {
        UserEntity user = getUserByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public void updateUserRole(String username, String newRole) {
        UserEntity user = getUserByUsername(username);
        user.setRole(UserEntity.Role.fromString(newRole));
        userRepository.save(user);
    }
}
