package com.matching.service.kafka;

import com.common.model.RideEvent;
import com.common.model.RideEventStatus;
import com.matching.service.client.DriverServiceClient;
import com.matching.service.client.RideServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideRequestListener {

    private final DriverServiceClient driverServiceClient;
    private final RideServiceClient rideServiceClient;

    @KafkaListener(topics = "ride_event", groupId = "matching-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleRideEvent(RideEvent event) {
        if (event.getStatus() != RideEventStatus.REQUESTED) return;

        log.info("Ride REQUESTED received for rideId={}, finding available driver...", event.getRideId());

        List<Map<String, Object>> availableDrivers = driverServiceClient.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            log.warn("No available drivers for rideId={}", event.getRideId());
            return;
        }

        Long driverId = ((Number) availableDrivers.get(0).get("driverId")).longValue();
        log.info("Assigning driverId={} to rideId={}", driverId, event.getRideId());
        rideServiceClient.assignDriver(event.getRideId(), driverId);
    }
}
