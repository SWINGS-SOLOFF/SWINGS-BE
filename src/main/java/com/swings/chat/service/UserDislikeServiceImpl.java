package com.swings.chat.service;

import com.swings.chat.entity.UserDislikeEntity;
import com.swings.chat.repository.UserDislikeRepository;
import com.swings.notification.service.FCMService;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDislikeServiceImpl implements UserDislikeService {

    private final UserDislikeRepository userDislikeRepository;
    private final UserRepository userRepository;
    private final FCMService fcmService;

    @Override
    @Transactional
    public void dislikeUser(String fromUsername, String toUsername) {

        userDislikeRepository.save(UserDislikeEntity.builder()
                .fromUsername(fromUsername)
                .toUsername(toUsername)
                .build());
    }
}