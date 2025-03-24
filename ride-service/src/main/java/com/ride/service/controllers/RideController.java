package com.ride.service.controllers;

import com.ride.service.entity.Ride;
import com.ride.service.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ride")
public class RideController {
    @Autowired
    private RideService rideService;
    @PostMapping("/request")
    public ResponseEntity<Ride> requestRide(@RequestParam String userId, @RequestParam String pickup, @RequestParam String drop) {
        return new ResponseEntity<>(rideService.requestRide(userId, pickup, drop), HttpStatus.CREATED);
    }

    @PostMapping("/assign/{rideId}")
    public Ride assignDriver(@PathVariable Long rideId, @RequestParam String driverId) {
        return rideService.assignDriver(rideId, driverId);
    }

    @PostMapping("/complete/{rideId}")
    public Ride completeRide(@PathVariable Long rideId) {
        return rideService.completeRide(rideId);
    }
}
