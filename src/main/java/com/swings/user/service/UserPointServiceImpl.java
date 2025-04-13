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

        // 🔥 핵심 조건: 포인트 부족 시 예외 던져서 400으로 응답 처리
        if (user.getPointBalance() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        userPointRepository.save(UserPointEntity.builder()
                .user(user)
                .amount(-amount) // 💸 사용은 음수로 기록
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
