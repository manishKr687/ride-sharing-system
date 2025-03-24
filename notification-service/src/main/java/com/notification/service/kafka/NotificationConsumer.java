package com.notification.service.kafka;

import com.notification.service.entity.Notification;
import com.notification.service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    @Autowired
    private NotificationRepository notificationRepository;

    @KafkaListener(topics = "notification_topic", groupId = "notification-group")
//    @Transactional   // It insure proper commit of transaction into database
    public void consumeNotification(NotificationEvent notificationEvent){
        System.out.println("Received Notification: " + notificationEvent);

        Notification notification = Notification.builder()
                .email(notificationEvent.getEmail())
                .message(notificationEvent.getMessage())
                .sent(true)
                .build();

        notificationRepository.save(notification);

        System.out.println("Notification sent to "+ notificationEvent.getEmail());
    }
}
