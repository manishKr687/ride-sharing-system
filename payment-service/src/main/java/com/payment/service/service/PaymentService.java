package com.payment.service.service;

import com.common.model.PaymentEvent;
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

    public void paymentProcess(Payment payment){
        //Payment Logic
        boolean paymentStatus = new Random().nextBoolean(); // Simulate Payment Success/Failure
        String status = paymentStatus ? "SUCCESS" : "FAILED";
        String message = paymentStatus ? "Payment Processed Successfully" : "Payment Failed";

        paymentRepository.save(payment);

        PaymentEvent paymentEvent = PaymentEvent.builder().
                paymentId(payment.getPaymentId()).
                userId(payment.getUserId()).
                rideId(payment.getRideId()).
                amount(payment.getAmount()).
                status(status).
                message(message).build();

        paymentProducer.sendPaymentEvent(paymentEvent);
        log.info("Payment event prepared and sent for paymentId={}, rideId={}", payment.getPaymentId(), payment.getRideId());
    }
}
