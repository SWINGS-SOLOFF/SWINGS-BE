package com.swings.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    private String paymentKey;
    private String orderId;
    private Long amount;
    private Long customerId; //  추가: 프론트에서 유저 ID 전달
    private String createdAt; // ISO 문자열로 받기

}
