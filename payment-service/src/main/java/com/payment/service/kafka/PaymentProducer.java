package com.payment.service.kafka;

import com.common.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {
    private static final String TOPIC = "payment_event";
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent paymentEvent){
        kafkaTemplate.send(TOPIC,paymentEvent);
        log.info("Payment event published to topic {} for paymentId={}", TOPIC, paymentEvent.getPaymentId());
    }
}
