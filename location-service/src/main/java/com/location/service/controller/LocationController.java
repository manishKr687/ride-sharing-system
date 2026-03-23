package com.location.service.controller;

import com.location.service.dto.NearbyDriverDTO;
import com.location.service.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PutMapping("/drivers/{driverId}")
    public ResponseEntity<String> updateDriverLocation(
            @PathVariable Long driverId,
            @RequestParam Long userId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        locationService.updateLocation(driverId, userId, latitude, longitude);
        return ResponseEntity.ok("Location updated");
    }

    @GetMapping("/drivers/nearby")
    public ResponseEntity<List<NearbyDriverDTO>> getNearbyDrivers(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        return ResponseEntity.ok(locationService.getNearbyDrivers(lat, lng, radiusKm));
    }
}
