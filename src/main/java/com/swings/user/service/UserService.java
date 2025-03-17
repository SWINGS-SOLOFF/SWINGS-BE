package com.swings.user.service;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(UserDTO UserDTO) {
        Optional<UserEntity> userOptional = userRepository.findById(UserDTO.getId());

        if (userOptional.isEmpty())     {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(UserDTO.getPassword(), user.getPassword())) {
             throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return jwtService.generateToken(String.valueOf(user.getId())); // JWT 반환

    }

    //사용자 등록
    public UserEntity registerUser(UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        return userRepository.save(user);
    }

    //사용자 UserId 조회(auto 숫자)
    public UserEntity findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    //사용자 Id 조회
    public UserEntity findById(String id){
        Optional<UserEntity> user = userRepository.findById(id);
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

        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(user.getUserId());
        }
    }

}
