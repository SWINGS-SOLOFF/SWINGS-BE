package com.swings.user.service;

import com.swings.user.dto.UserPointDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.entity.UserPointEntity;
import com.swings.user.entity.UserPointEntity.PointType;
import com.swings.user.repository.UserPointRepository;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPointServiceImpl implements UserPointService {

    private final UserPointRepository userPointRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserPointDTO> findPointLogByUsername(String username) {
        return userPointRepository.findPointLogByUsername(username).stream()
                .map(p -> UserPointDTO.builder()
                        .userId(p.getUser().getUserId())
                        .amount(p.getAmount())
                        .type(p.getType())
                        .description(p.getDescription())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void chargePoint(String username, int amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        UserEntity user = getUser(username);

        userPointRepository.save(UserPointEntity.builder()
                .user(user)
                .amount(amount)
                .type(PointType.CHARGE)
                .description(description)
                .build());

        user.setPointBalance(user.getPointBalance() + amount);
    }

    @Override
    @Transactional
    public void usePoint(String username, int amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");

        UserEntity user = getUser(username);

        if (user.getPointBalance() < amount) {
            // 400 에러로 명확히 떨어지게 수정
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.");
        }

        userPointRepository.save(UserPointEntity.builder()
                .user(user)
                .amount(-amount)
                .type(PointType.USE)
                .description(description)
                .build());

        user.setPointBalance(user.getPointBalance() - amount);
    }


    private UserEntity getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }
}
