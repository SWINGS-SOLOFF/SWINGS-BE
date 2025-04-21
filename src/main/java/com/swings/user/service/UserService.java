package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    boolean isUsernameExists(String username); // 아이디 중복 확인
    UserEntity registerUser(UserDTO dto); // 회원가입
    UserEntity getUserByUsername(String username); // 특정 사용자 조회
    UserEntity getCurrentUser(); // 현재 로그인한 사용자 조회
    UserDTO getCurrentUserDto(); // 현재 로그인한 사용자 DTO 반환
    UserDTO convertToDto(UserEntity user); // UserEntity -> DTO 변환
    UserEntity updateUser(String username, UserDTO dto); // 사용자 정보 수정
    void deleteCurrentUserWithPassword(String password); // 비밀번호를 확인하여 회원탈퇴
    void updateProfileImage(MultipartFile image); //프로필 사진 수정

    // 관리자 페이지
    List<UserEntity> getAllUsers(); // (관리자용 내부 호출용 - 유지)
    List<UserDTO> getAllUsersDto(); // 관리자 페이지용 전체 사용자 DTO 반환
    void deleteUserByUsername(String username);
    void updateUserRole(String username, String newRole);

    //비밀번호 리셋
    void resetPassword(String username, String email);

    // 푸시 알림용 토큰
    void updatePushToken(String username, String token);

}