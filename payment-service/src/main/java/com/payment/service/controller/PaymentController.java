package com.payment.service.controller;

import com.payment.service.dto.PaymentRequestDTO;
import com.payment.service.dto.PaymentResponseDTO;
import com.payment.service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<PaymentResponseDTO> sentPaymentRequest(@Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.paymentProcess(request));
    }
}
