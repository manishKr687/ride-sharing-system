package com.payment.service.kafka;

import com.common.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
    private static final String TOPIC = "payment_event";
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent paymentEvent){
        kafkaTemplate.send(TOPIC,paymentEvent);
        System.out.println("Payment Event Sent: " + paymentEvent);
    }
}
