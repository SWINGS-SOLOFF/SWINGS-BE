package com.swings.payment.service;

import com.swings.payment.dto.PaymentRequestDTO;

public interface PaymentService {
    String confirmPayment(PaymentRequestDTO requestDTO);
}
