package com.location.service.kafka;

import com.common.model.LocationUpdateEvent;
import com.location.service.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationEventListener {

    private final LocationService locationService;

    @KafkaListener(topics = "location_event", groupId = "location-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleLocationUpdate(LocationUpdateEvent event) {
        log.info("Location update received for driverId={}", event.getDriverId());
        locationService.updateLocation(
                event.getDriverId(),
                event.getUserId(),
                event.getLatitude(),
                event.getLongitude()
        );
    }
}
