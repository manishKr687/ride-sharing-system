package com.ride.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String RIDE_NOTIFY_TOPIC = "ride_notify_topic";

    public void notifyDriver(String message) {
        kafkaTemplate.send(RIDE_NOTIFY_TOPIC, message);
    }
}
