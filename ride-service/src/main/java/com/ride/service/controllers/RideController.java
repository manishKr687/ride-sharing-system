package com.ride.service.controllers;

import com.common.model.RideEvent;
import com.ride.service.entity.Ride;
import com.ride.service.entity.RideStatus;
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
    public ResponseEntity<Ride> requestRide(@RequestBody Ride ride) {
        return new ResponseEntity<>(rideService.requestRide(ride), HttpStatus.CREATED);
    }

    @PostMapping("/assign/{rideId}")
    public Ride assignDriver(@PathVariable Long rideId, @RequestParam String driverId) {
        return rideService.assignDriver(rideId, driverId);
    }
    @PutMapping("/{rideId}/status/{status}")
    public Ride updateRideStatus(@PathVariable Long rideId, @PathVariable String status) {
        RideStatus rideStatus = RideStatus.valueOf(status.toUpperCase());
        return rideService.updateRideStatus(rideId, rideStatus);
    }
    @PostMapping("/complete/{rideId}")
    public Ride completeRide(@PathVariable Long rideId) {
        return rideService.completeRide(rideId);
    }
}
