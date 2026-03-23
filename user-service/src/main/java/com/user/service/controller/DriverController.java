package com.user.service.controller;

import com.user.service.dto.DriverRequestDTO;
import com.user.service.dto.DriverResponseDTO;
import com.user.service.entity.DriverStatus;
import com.user.service.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/drivers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<DriverResponseDTO> registerDriver(@Valid @RequestBody DriverRequestDTO request) {
        return new ResponseEntity<>(driverService.registerDriver(request), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverResponseDTO> getDriverByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(driverService.getDriverByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverResponseDTO>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PutMapping("/update-status/{driverId}")
    public ResponseEntity<DriverResponseDTO> updateDriverStatus(
            @PathVariable Long driverId,
            @RequestParam DriverStatus status) {
        return ResponseEntity.ok(driverService.updateDriverStatus(driverId, status));
    }

    @PutMapping("/update-location/{driverId}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable Long driverId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        driverService.updateDriverLocation(driverId, latitude, longitude);
        return ResponseEntity.noContent().build();
    }
}
