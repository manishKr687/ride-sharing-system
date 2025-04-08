package com.payment.service.service;

import com.common.model.PaymentEvent;
import com.payment.service.entity.PaymentRequest;
import com.payment.service.kafka.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {
    @Autowired
    private PaymentProducer paymentProducer;
    public void paymentProcess(PaymentRequest paymentRequest){
        //Payment Logic
        boolean paymentStatus = new Random().nextBoolean(); // Simulate Payment Success/Failure
        String status = paymentStatus ? "SUCCESS" : "FAILED";
        String message = paymentStatus ? "Payment Processed Successfully" : "Payment Failed";

        PaymentEvent paymentEvent = PaymentEvent.builder().
                userId(paymentRequest.getUserId()).
                rideId(paymentRequest.getRideId()).
                amount(paymentRequest.getAmount()).
                status(status).
                message(message).build();

        paymentProducer.sendPaymentEvent(paymentEvent);
        System.out.println("Payment Event Sent");
    }
}
