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
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WebClient tossWebClient;
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 코인 단위 환산 기준
    private static final int COIN_UNIT_PRICE = 1000;

    @Override
    public String confirmPayment(PaymentRequestDTO requestDTO) {
        try {
            String response = tossWebClient.post()
                    .uri("/payments/confirm")
                    .bodyValue(requestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Toss API 응답 수신: " + response);

            JsonNode root = objectMapper.readTree(response);
            long userId = requestDTO.getCustomerId();
            int amount = root.get("totalAmount").asInt();
            int coin = amount / COIN_UNIT_PRICE;

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

// createdAt 파싱 (OffsetDateTime → LocalDateTime으로 변환)
            LocalDateTime createdAt = (requestDTO.getCreatedAt() != null)
                    ? OffsetDateTime.parse(requestDTO.getCreatedAt()).toLocalDateTime()
                    : LocalDateTime.now();

            user.setPointBalance(user.getPointBalance() + coin);
            userRepository.save(user);

            // createdAt 설정 포함
            UserPointEntity log = UserPointEntity.builder()
                    .user(user)
                    .amount(coin)
                    .type(UserPointEntity.PointType.CHARGE)
                    .description("토스 결제 충전")
                    .createdAt(createdAt)
                    .build();

            userPointRepository.save(log);
            return response;

        } catch (WebClientResponseException e) {
            System.err.println("Toss API 응답 에러: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("결제 처리 중 예외 발생", e);
        }
    }
}