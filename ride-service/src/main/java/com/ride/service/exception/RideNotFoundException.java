package com.ride.service.exception;

public class RideNotFoundException extends RuntimeException {
    private final Long rideId;
    public RideNotFoundException(Long rideId) {
        super("Ride not found with ID: " + rideId);
        this.rideId = rideId;
    }

    public Long getRideId() {
        return rideId;
    }
}
