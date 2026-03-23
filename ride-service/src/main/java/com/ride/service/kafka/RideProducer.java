package com.ride.service.kafka;


import com.common.model.RideEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideProducer {
    private static final String RIDE_TOPIC = "ride_event";

    private final KafkaTemplate<String, RideEvent> kafkaTemplate;

    public void sendRideEvent(RideEvent rideEvent) {
        kafkaTemplate.send(RIDE_TOPIC, rideEvent);
        log.info("Ride event published to topic {} for rideId={} with status={}", RIDE_TOPIC, rideEvent.getRideId(), rideEvent.getStatus());
    }
}

