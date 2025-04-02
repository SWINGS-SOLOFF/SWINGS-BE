package com.swings.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swings.payment.dto.PaymentRequestDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.entity.UserPointEntity;
import com.swings.user.repository.UserPointRepository;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WebClient tossWebClient; // TossConfig에서 Bean으로 등록한 WebClient
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String confirmPayment(PaymentRequestDTO requestDTO) {
        String response = tossWebClient.post()
                .uri("/payments/confirm")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // ✅ 결제 성공 후 포인트 자동 충전
        try {
            JsonNode root = objectMapper.readTree(response);

            long userId = Long.parseLong(root.get("customerName").asText());
            int amount = root.get("amount").asInt();

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            // 포인트 잔액 증가
            user.setPointBalance(user.getPointBalance() + amount);
            userRepository.save(user);

            // 포인트 내역 저장
            UserPointEntity log = UserPointEntity.builder()
                    .user(user)
                    .amount(amount)
                    .type(UserPointEntity.PointType.CHARGE)
                    .description("토스 결제 충전")
                    .build();
            userPointRepository.save(log);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
