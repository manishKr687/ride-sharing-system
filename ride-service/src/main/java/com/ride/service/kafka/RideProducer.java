package com.ride.service.kafka;


import com.common.model.RideEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideProducer {
        private static final String RIDE_TOPIC = "ride_request_topic";

        @Autowired
        public KafkaTemplate<String, RideEvent> kafkaTemplate;

        public void sendRideEvent(RideEvent rideEvent) {
            kafkaTemplate.send(RIDE_TOPIC, rideEvent);
            System.out.println("Sent Ride Event: " + rideEvent);
        }
}

