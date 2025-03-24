package com.notification.service.kafka;

import com.notification.service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private static final String TOPIC = "notification_topic";

    @Autowired
    private KafkaTemplate<String,NotificationEvent> kafkaTemplate;

    public void sendNotification(NotificationEvent notificationEvent){
        kafkaTemplate.send(TOPIC,notificationEvent);
    }
}
