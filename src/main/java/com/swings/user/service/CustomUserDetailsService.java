package com.swings.user.service;

import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        UserEntity user = userEntityOptional.orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId())
                .password(user.getPassword())
                .roles(user.getRole() != null ? user.getRole().name() : "USER") // 기본값 추가
                .build();

    }

    public UserDetails loadUserById(String id) {
        return loadUserByUsername(id);
    }

}
