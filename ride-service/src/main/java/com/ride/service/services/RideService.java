package com.ride.service.services;

import com.ride.service.entity.Ride;
import com.ride.service.entity.RideStatus;
import com.ride.service.kafka.RideEvent;
import com.ride.service.kafka.RideProducer;
import com.ride.service.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideService {
    @Autowired
    private RideProducer rideProducer;
    @Autowired
    private RideRepository rideRepository;

    public Ride requestRide(Ride ride){
        ride.setStatus(RideStatus.REQUESTED);
        ride.setStartTime(LocalDateTime.now());
        Ride savedRide = rideRepository.save(ride);

        // Send Kafka Event
        RideEvent rideEvent = RideEvent.builder()
                .rideId(savedRide.getRideId())
                .userId(savedRide.getUserId())
                .driverId(savedRide.getDriverId())
                .status("REQUESTED")
                .message("Ride Requested Successfully")
                .build();

        rideProducer.sendRideEvent(rideEvent);
        return savedRide;
    }
    public Ride updateRideStatus(Long rideId, RideStatus status) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found!"));
        ride.setStatus(status);

        if (status == RideStatus.COMPLETED) {
            ride.setEndTime(LocalDateTime.now());
        }

        Ride updatedRide = rideRepository.save(ride);

        // Send Kafka Event
        RideEvent rideEvent = RideEvent.builder()
                .rideId(updatedRide.getRideId())
                .userId(updatedRide.getUserId())
                .driverId(updatedRide.getDriverId())
                .status(status.toString())
                .message("Ride Status Updated to: " + status)
                .build();

        rideProducer.sendRideEvent(rideEvent);
        return updatedRide;
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
