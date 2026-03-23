package com.matching.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.ride-service.base-url:http://localhost:7071}")
    private String rideServiceBaseUrl;

    public void assignDriver(Long rideId, Long driverId) {
        try {
            String url = rideServiceBaseUrl + "/api/v1/ride/assign/" + rideId + "?driverId=" + driverId;
            restTemplate.postForObject(url, null, Object.class);
            log.info("Driver {} assigned to ride {}", driverId, rideId);
        } catch (Exception e) {
            log.error("Failed to assign driver {} to ride {}: {}", driverId, rideId, e.getMessage());
        }
    }
}
