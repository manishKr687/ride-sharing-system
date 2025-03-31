package com.notification.service.kafka;

import com.common.model.RideEvent;
import com.notification.service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final EmailService emailService;

    @Autowired
    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "ride_event", groupId = "notification-group")
    public void listenRideEvents(RideEvent rideEvent) {
        System.out.println("Received Ride Event: " + rideEvent);

        try {
            // Send email notification after processing
            emailService.sendEmail(
                    "mfresher687@gmail.com",  // Replace with actual recipient
                    "Ride Event Notification",
                    rideEvent.getStatus()
            );
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        // Handle ride event status
        switch (rideEvent.getStatus()) {
            case "REQUESTED":
                System.out.println("Processing new ride request...");
                break;
            case "ACCEPTED":
                System.out.println("Driver accepted the ride...");
                break;
            case "COMPLETED":
                System.out.println("Ride completed successfully...");
                break;
            default:
                System.out.println("Unknown event status received: " + rideEvent.getStatus());
        }
    }


}
