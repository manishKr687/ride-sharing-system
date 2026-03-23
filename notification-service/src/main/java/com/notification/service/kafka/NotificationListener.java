package com.notification.service.kafka;


import com.common.model.RideEvent;
import com.notification.service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final EmailService emailService;

    @Value("${notification.recipient.email}")
    private String recipientEmail;

    @KafkaListener(topics = "ride_event", groupId = "notification-group", containerFactory = "rideKafkaListenerContainerFactory")
    public void listenRideEvents(RideEvent rideEvent) {
        log.info("Received ride event for rideId={} with status={}", rideEvent.getRideId(), rideEvent.getStatus());

        try {
            // Send email notification after processing
            emailService.sendEmail(
                    recipientEmail,
                    "Ride Event Notification",
                    rideEvent.getStatus().name()
            );
            log.info("Ride notification email sent for rideId={}", rideEvent.getRideId());
        } catch (Exception e) {
            log.error("Failed to send ride notification email for rideId={}", rideEvent.getRideId(), e);
        }

        // Handle ride event status
        switch (rideEvent.getStatus()) {
            case REQUESTED:
                log.info("Processing new ride request for rideId={}", rideEvent.getRideId());
                break;
            case ASSIGNED:
                log.info("Driver assigned to rideId={} with driverId={}", rideEvent.getRideId(), rideEvent.getDriverId());
                break;
            case COMPLETED:
                log.info("Ride completed successfully for rideId={}", rideEvent.getRideId());
                break;
            default:
                log.warn("Unknown ride event status received for rideId={}: {}", rideEvent.getRideId(), rideEvent.getStatus());
        }
    }
}
