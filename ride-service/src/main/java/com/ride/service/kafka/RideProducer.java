package com.ride.service.kafka;


import com.common.model.RideEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideProducer {
    private static final String RIDE_TOPIC = "ride_event";

    private final KafkaTemplate<String, RideEvent> kafkaTemplate;

    public void sendRideEvent(RideEvent rideEvent) {
        kafkaTemplate.send(RIDE_TOPIC, rideEvent);
        System.out.println("Sent Ride Event: " + rideEvent);
    }
}

