package com.swings.user.service;

import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //사용자 등록
    public UserEntity registerUser(UserEntity user){
        return userRepository.save(user);
    }

    //사용자 UserId 조회(auto 숫자)
    public UserEntity findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    //사용자 Id 조회
    public UserEntity findById(String id){
        Optional<UserEntity> user = userRepository.findByUserId(id);
        return user.orElse(null);
    }


    //모든 사용자 조회
    public List<UserEntity> findAllUser() {
        return  userRepository.findAll();
    }

    //사용자 수정
    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

    //사용자 삭제
    public void deleteUser(String id) {

        UserEntity user = userRepository.findByUserId(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(user.getUserId());
        }
    }

}
