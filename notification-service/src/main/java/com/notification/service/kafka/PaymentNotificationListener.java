package com.notification.service.kafka;

import com.common.model.PaymentEvent;
import com.notification.service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentNotificationListener {
    private final EmailService emailService;

    @KafkaListener(topics = "payment_event", groupId = "notification-group", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listenPaymentEvents(PaymentEvent paymentEvent) {
        log.info("Received payment event for paymentId={} with status={}", paymentEvent.getPaymentId(), paymentEvent.getStatus());

        try {
            // Send email notification after processing
            emailService.sendEmail(
                    "mfresher687@gmail.com",
                    "Payment Event Notification",
                    paymentEvent.getStatus()
            );
            log.info("Payment notification email sent for paymentId={}", paymentEvent.getPaymentId());
        } catch (Exception e) {
            log.error("Failed to send payment notification email for paymentId={}", paymentEvent.getPaymentId(), e);
        }

        // Handle payment event status
        switch (paymentEvent.getStatus()) {
            case "SUCCESS":
                log.info("Payment successful for paymentId={}", paymentEvent.getPaymentId());
                break;
            default:
                log.warn("Payment failed for paymentId={} with status={}", paymentEvent.getPaymentId(), paymentEvent.getStatus());
        }
    }
}
