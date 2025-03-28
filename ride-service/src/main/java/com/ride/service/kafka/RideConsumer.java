package com.ride.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RideConsumer {
    @KafkaListener(topics = "ride_request_topic", groupId = "ride-group")
    public void listenRideEvents(RideEvent rideEvent) {
        System.out.println("Received Ride Event: " + rideEvent);

        // Process received events
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
                System.out.println("Unknown event received.");
        }
    }
}
