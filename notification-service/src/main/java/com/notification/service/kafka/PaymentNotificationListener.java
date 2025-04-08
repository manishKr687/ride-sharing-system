package com.notification.service.kafka;

import com.common.model.PaymentEvent;
import com.notification.service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationListener {
    private final EmailService emailService;

    @Autowired
    public PaymentNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }
    @KafkaListener(topics = "payment_event", groupId = "notification-group", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listenPaymentEvents(PaymentEvent paymentEvent) {
        System.out.println("Received Payment Event: " + paymentEvent);

        try {
            // Send email notification after processing
            emailService.sendEmail(
                    "mfresher687@gmail.com",
                    "Payment Event Notification",
                    paymentEvent.getStatus()
            );
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        // Handle payment event status
        switch (paymentEvent.getStatus()) {
            case "SUCCESS":
                System.out.println("Payment Successful...");
                break;
            default:
                System.out.println("Payment Failed: " + paymentEvent.getStatus());
        }
    }
}
