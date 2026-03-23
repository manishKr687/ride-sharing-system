package com.location.service.service;

import com.location.service.dto.NearbyDriverDTO;
import com.location.service.entity.DriverLocation;
import com.location.service.repository.DriverLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final DriverLocationRepository locationRepository;

    @Transactional
    public void updateLocation(Long driverId, Long userId, Double latitude, Double longitude) {
        DriverLocation location = locationRepository.findByDriverId(driverId)
                .orElse(DriverLocation.builder().driverId(driverId).build());

        location.setUserId(userId);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setUpdatedAt(LocalDateTime.now());
        locationRepository.save(location);
        log.info("Location updated for driverId={}: ({}, {})", driverId, latitude, longitude);
    }

    public List<NearbyDriverDTO> getNearbyDrivers(Double lat, Double lng, Double radiusKm) {
        return locationRepository.findAllByLatitudeNotNullAndLongitudeNotNull().stream()
                .map(loc -> {
                    double distance = haversine(lat, lng, loc.getLatitude(), loc.getLongitude());
                    return NearbyDriverDTO.builder()
                            .driverId(loc.getDriverId())
                            .userId(loc.getUserId())
                            .latitude(loc.getLatitude())
                            .longitude(loc.getLongitude())
                            .distanceKm(Math.round(distance * 100.0) / 100.0)
                            .build();
                })
                .filter(dto -> dto.getDistanceKm() <= radiusKm)
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
