package com.swings.chat.service;

import com.swings.chat.entity.UserDislikeEntity;
import com.swings.chat.repository.UserDislikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDislikeService {

    private final UserDislikeRepository userDislikeRepository;

    @Transactional
    public void dislikeUser(String fromUsername, String toUsername) {
        userDislikeRepository.save(UserDislikeEntity.builder()
                .fromUsername(fromUsername)
                .toUsername(toUsername)
                .build());
    }
}
