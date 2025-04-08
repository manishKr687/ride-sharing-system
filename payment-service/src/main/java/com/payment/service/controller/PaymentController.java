package com.payment.service.controller;

import com.payment.service.entity.PaymentRequest;
import com.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("")
    public String sentPaymentRequest(@RequestBody PaymentRequest paymentRequest){
        paymentService.paymentProcess(paymentRequest);
        return "Payment Request Sent";
    }
}
