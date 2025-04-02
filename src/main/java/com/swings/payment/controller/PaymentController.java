package com.swings.payment.controller;

import com.swings.payment.dto.PaymentRequestDTO;
import com.swings.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentRequestDTO requestDTO) {
        String response = paymentService.confirmPayment(requestDTO);
        return ResponseEntity.ok(response);
    }
}
