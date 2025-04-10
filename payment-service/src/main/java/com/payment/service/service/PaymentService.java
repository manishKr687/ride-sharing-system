package com.payment.service.service;

import com.common.model.PaymentEvent;
import com.payment.service.entity.Payment;
import com.payment.service.kafka.PaymentProducer;
import com.payment.service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {
    @Autowired
    private PaymentProducer paymentProducer;

    @Autowired
    private PaymentRepository paymentRepository;
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
        System.out.println("Payment Event Sent");
    }
}
