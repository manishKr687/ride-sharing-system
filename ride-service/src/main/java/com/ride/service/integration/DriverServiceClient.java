package com.ride.service.integration;

import com.ride.service.exception.RideProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.driver-service.base-url:http://driver-service:8082}")
    private String driverServiceBaseUrl;

    public void assignDriver(Long driverId) {
        String endpoint = String.format(
            "%s/api/v2/drivers/update-status/%d?status=ASSIGNED",
            driverServiceBaseUrl,
            driverId
        );

        try {
            restTemplate.put(endpoint, null);
            log.info("Driver {} marked as ASSIGNED in driver-service", driverId);
        } catch (RestClientException ex) {
            log.error("Failed to update driver {} status in driver-service", driverId, ex);
            throw new RideProcessingException(
                "Failed to coordinate driver assignment with driver-service",
                ex
            );
        }
    }
}
