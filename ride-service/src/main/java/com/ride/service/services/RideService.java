package com.ride.service.services;

import com.ride.service.entity.Ride;
import com.ride.service.entity.RideStatus;
import com.ride.service.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride requestRide(String userId, String pickup, String drop){
        Ride ride = new Ride();
        ride.setUserId(userId);
        ride.setPickupLocation(pickup);
        ride.setDropLocation(drop);
        ride.setStatus(RideStatus.REQUESTED);
        return rideRepository.save(ride);
    }

    public Ride assignDriver(Long rideId, String driverId){
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RuntimeException("Ride Not Found!"));
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ASSIGNED);
        return rideRepository.save(ride);
    }

    public Ride completeRide(Long rideId){
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RuntimeException("Ride Not Found!"));
        ride.setStatus(RideStatus.COMPLETED);
        return rideRepository.save(ride);
    }
}
