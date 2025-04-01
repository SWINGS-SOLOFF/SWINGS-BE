package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;

import java.util.List;

public interface UserService {
    boolean isUsernameExists(String username); // 아이디 중복 확인
    UserEntity registerUser(UserDTO dto); // 회원가입
    UserEntity getUserByUsername(String username); // 특정 사용자 조회
    UserEntity getCurrentUser(); // 현재 로그인한 사용자 조회
    UserEntity updateUser(String username, UserDTO dto); // 사용자 정보 수정
    void deleteCurrentUserWithPassword(String password); // 비밀번호를 확인하여 회원탈퇴

    //관리자페이지
    List<UserEntity> getAllUsers();
    void deleteUserByUsername(String username);
    void updateUserRole(String username, String newRole);


}
