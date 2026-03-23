package com.matching.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.driver-service.base-url:http://localhost:7073}")
    private String driverServiceBaseUrl;

    public List<Map<String, Object>> getAvailableDrivers() {
        try {
            var response = restTemplate.exchange(
                    driverServiceBaseUrl + "/api/v2/drivers/available",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to fetch available drivers: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
