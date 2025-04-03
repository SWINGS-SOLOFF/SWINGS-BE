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

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WebClient tossWebClient;
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String confirmPayment(PaymentRequestDTO requestDTO) {
        try {
            // Toss 결제 확인 요청
            String response = tossWebClient.post()
                    .uri("/payments/confirm")
                    .bodyValue(requestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("✅ Toss API 응답 수신: " + response);

            JsonNode root = objectMapper.readTree(response);

            // 🔥 여기 수정: 프론트에서 넘긴 customerId 사용
            long userId = requestDTO.getCustomerId();
            int amount = root.get("totalAmount").asInt(); // or "amount"

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            // 포인트 잔액 업데이트
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

            return response;

        } catch (WebClientResponseException e) {
            System.err.println("❌ Toss API 응답 에러: " + e.getResponseBodyAsString());
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("결제 처리 중 예외 발생", e);
        }
    }
}
