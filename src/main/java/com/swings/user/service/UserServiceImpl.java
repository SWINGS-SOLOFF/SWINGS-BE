package com.swings.user.service;

import com.swings.email.service.EmailService;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
                .isVerified(false) // 이메일 인증 (default 값은 인증X) 인증 후 로그인 가능
                .build();

        UserEntity savedUser = userRepository.save(user);
        emailService.sendEmailVerification(savedUser);
        System.out.println("[이메일 인증 메일 전송됨]: " + user.getEmail());

        return savedUser;
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
    public UserDTO getCurrentUserDto() {
        return convertToDto(getCurrentUser());
    }

    @Override
    public UserDTO convertToDto(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setPassword(null);
        dto.setName(user.getName());
        dto.setBirthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : "1900-01-01");
        dto.setPhonenumber(user.getPhonenumber() != null ? user.getPhonenumber() : "000-0000-0000");
        dto.setEmail(user.getEmail() != null ? user.getEmail() : "unknown@email.com");
        dto.setJob(user.getJob() != null ? user.getJob() : "unknown");
        dto.setGolfSkill(user.getGolfSkill() != null ? user.getGolfSkill().name() : "beginner");
        dto.setMbti(user.getMbti() != null ? user.getMbti() : "NONE");
        dto.setHobbies(user.getHobbies() != null ? user.getHobbies() : "");
        dto.setReligion(user.getReligion() != null ? user.getReligion() : "무교");
        dto.setSmoking(user.getSmoking() != null ? user.getSmoking().name() : "no");
        dto.setDrinking(user.getDrinking() != null ? user.getDrinking().name() : "no");
        dto.setIntroduce(user.getIntroduce() != null ? user.getIntroduce() : "");
        dto.setUserImg(user.getUserImg());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "player");
        dto.setGender(user.getGender() != null ? user.getGender().name() : "male");
        dto.setActivityRegion(user.getActivityRegion() != null ? user.getActivityRegion().name() : "SEOUL");
        dto.setIsVerified(user.isVerified()); // 추가
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

        // 인증 상태 업데이트
        if (dto.getIsVerified() != null) {
            user.setVerified(dto.getIsVerified());
        }

        return userRepository.save(user);
    }

    @Override
    public void updateProfileImage(MultipartFile image) {
        UserEntity user = getCurrentUser(); // 현재 로그인한 사용자 가져오기

        if (image != null && !image.isEmpty()) {
            try {
                // 파일 저장 경로 설정
                String uploadDir = "C:/uploads/"; // 업로드 디렉토리
                String originalFilename = image.getOriginalFilename();
                String savedFilename = UUID.randomUUID() + "_" + originalFilename; // 고유한 파일명 생성
                File dest = new File(uploadDir + savedFilename);

                // 기존 프로필 이미지 삭제 (이전 이미지를 덮어쓸 때 삭제)
                if (user.getUserImg() != null && !user.getUserImg().isEmpty()) {
                    File oldImage = new File(uploadDir + user.getUserImg()); // 기존 파일 경로
                    if (oldImage.exists()) {
                        oldImage.delete(); // 이전 파일 삭제
                    }
                }

                // 새로운 이미지 저장
                image.transferTo(dest); // 파일을 디스크에 저장

                // 사용자 엔티티 업데이트 (프로필 이미지 파일명 업데이트)
                user.setUserImg(savedFilename); // DB에 새로운 파일명 저장
                userRepository.save(user); // 사용자 정보 업데이트

            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("이미지가 비어 있습니다.");
        }
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
    public List<UserDTO> getAllUsersDto() {
        return userRepository.findAll().stream()
                .map(user -> {
                    try {
                        return convertToDto(user);
                    } catch (Exception e) {
                        System.out.println("DTO 변환 실패: userId=" + user.getUserId() + " → " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .toList();
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

    //비밀번호 리셋
    @Override
    public void resetPassword(String username, String email) {
        // username + email 둘 다 일치하는 사용자 찾기
        UserEntity user = userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 이메일이 일치하지 않습니다."));

        // 임시 비밀번호 생성 (8자리)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // 비밀번호 암호화 후 저장
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        // 이메일 전송
        emailService.sendTemporaryPassword(user, tempPassword);
    }


    // 푸쉬 알림용 토큰
    @Override
    public void updatePushToken(String username, String token) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        user.setPushToken(token);
        userRepository.save(user);
    }

}