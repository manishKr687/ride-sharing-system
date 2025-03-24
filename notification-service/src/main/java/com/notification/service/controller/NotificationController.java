package com.notification.service.controller;

import com.notification.service.entity.Notification;
import com.notification.service.kafka.NotificationEvent;
import com.notification.service.kafka.NotificationProducer;
import com.notification.service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationProducer notificationProducer;

    @PostMapping("/send")
    public String sendNotification(@RequestBody NotificationEvent notificationEvent) {
        notificationProducer.sendNotification(notificationEvent);
        return "Notification request sent successfully!";
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}