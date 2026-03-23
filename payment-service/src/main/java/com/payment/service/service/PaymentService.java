package com.payment.service.service;

import com.common.model.PaymentEvent;
import com.payment.service.dto.PaymentResponseDTO;
import com.payment.service.entity.Payment;
import com.payment.service.kafka.PaymentProducer;
import com.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentProducer paymentProducer;
    private final PaymentRepository paymentRepository;

    public PaymentResponseDTO paymentProcess(Payment payment) {
        boolean paymentStatus = new Random().nextBoolean();
        String status = paymentStatus ? "SUCCESS" : "FAILED";
        String message = paymentStatus ? "Payment Processed Successfully" : "Payment Failed";

        Payment savedPayment = paymentRepository.save(payment);

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .paymentId(savedPayment.getPaymentId())
                .userId(savedPayment.getUserId())
                .rideId(savedPayment.getRideId())
                .amount(savedPayment.getAmount())
                .status(status)
                .message(message)
                .build();

        paymentProducer.sendPaymentEvent(paymentEvent);
        log.info("Payment event prepared and sent for paymentId={}, rideId={}", savedPayment.getPaymentId(), savedPayment.getRideId());

        return PaymentResponseDTO.builder()
                .paymentId(savedPayment.getPaymentId())
                .rideId(savedPayment.getRideId())
                .userId(savedPayment.getUserId())
                .amount(savedPayment.getAmount())
                .status(status)
                .message(message)
                .build();
    }
}
