package com.payment.service.controller;

import com.payment.service.entity.Payment;
import com.payment.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public String sentPaymentRequest(@RequestBody Payment payment){
        paymentService.paymentProcess(payment);
        return "Payment Request Sent";
    }
}
