package com.ride.service.controllers;

import com.ride.service.dto.RideRequestDTO;
import com.ride.service.dto.RideResponseDTO;
import com.ride.service.dto.RideStatusUpdateDTO;
import com.ride.service.entity.RideStatus;
import com.ride.service.services.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor  // ✅ Constructor injection
@Validated  // ✅ Enable method-level validation
public class RideController {
    
    private final RideService rideService;  // ✅ Final field with constructor injection
    
    @PostMapping("/request")
    public ResponseEntity<RideResponseDTO> requestRide(
            @Valid @RequestBody RideRequestDTO request) {  // ✅ Added @Valid
        RideResponseDTO response = rideService.requestRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/assign/{rideId}")
    public ResponseEntity<RideResponseDTO> assignDriver(
            @PathVariable @Positive Long rideId,  // ✅ Validate path variable
            @RequestParam @Positive Long driverId) {  // ✅ Validate param
        RideResponseDTO response = rideService.assignDriver(rideId, driverId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{rideId}/status")
    public ResponseEntity<RideResponseDTO> updateRideStatus(
            @PathVariable @Positive Long rideId,
            @Valid @RequestBody RideStatusUpdateDTO statusUpdate) {
        RideResponseDTO response = rideService.updateRideStatus(
            rideId, 
            statusUpdate.getStatus()
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/complete/{rideId}")
    public ResponseEntity<RideResponseDTO> completeRide(
            @PathVariable @Positive Long rideId) {
        RideResponseDTO response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }
}
