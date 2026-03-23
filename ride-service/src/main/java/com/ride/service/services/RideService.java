package com.ride.service.services;

import com.common.model.RideEvent;
import com.common.model.RideEventStatus;
import com.ride.service.dto.RideRequestDTO;
import com.ride.service.dto.RideResponseDTO;
import com.ride.service.entity.Ride;
import com.ride.service.entity.RideStatus;
import com.ride.service.exception.InvalidRideStateException;
import com.ride.service.exception.RideNotFoundException;
import com.ride.service.exception.RideProcessingException;
import com.ride.service.integration.DriverServiceClient;
import com.ride.service.kafka.RideProducer;
import com.ride.service.repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
    
    private final RideProducer rideProducer;
    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;

    @Transactional
    public RideResponseDTO requestRide(RideRequestDTO request) {
        log.info("Processing ride request for user: {}", request.getUserId());
        
        try {
            // Map DTO to Entity
            Ride ride = Ride.builder()
                .userId(request.getUserId())
                .pickupLocation(request.getPickupLocation())
                .dropLocation(request.getDropLocation())
                .status(RideStatus.REQUESTED)
                .startTime(LocalDateTime.now())
                .build();
            
            // Save to database
            Ride savedRide = rideRepository.save(ride);
            log.debug("Ride saved with ID: {}", savedRide.getRideId());
            
            // Send Kafka Event
            RideEvent rideEvent = RideEvent.builder()
                .rideId(savedRide.getRideId())
                .userId(savedRide.getUserId())
                .driverId(savedRide.getDriverId())
                .status(RideEventStatus.REQUESTED)
                .message("Ride Requested Successfully")
                .build();
            
            rideProducer.sendRideEvent(rideEvent);
            log.info("Ride event published for ride ID: {}", savedRide.getRideId());
            
            // Map Entity to Response DTO
            return mapToResponseDTO(savedRide);
            
        } catch (Exception e) {
            log.error("Failed to process ride request for user: {}", request.getUserId(), e);
            throw new RideProcessingException("Failed to process ride request", e);
        }
    }
    
    @Transactional
    public RideResponseDTO updateRideStatus(Long rideId, RideStatus newStatus) {
        log.info("Updating ride {} status to {}", rideId, newStatus);
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        // Validate state transition
        validateStatusTransition(ride.getStatus(), newStatus);
        
        ride.setStatus(newStatus);
        
        if (newStatus == RideStatus.COMPLETED) {
            ride.setEndTime(LocalDateTime.now());
        }
        
        Ride updatedRide = rideRepository.save(ride);
        
        // Send Kafka Event
        RideEvent rideEvent = RideEvent.builder()
            .rideId(updatedRide.getRideId())
            .userId(updatedRide.getUserId())
            .driverId(updatedRide.getDriverId())
            .status(toRideEventStatus(newStatus))
            .message("Ride Status Updated to: " + newStatus)
            .build();
        
        rideProducer.sendRideEvent(rideEvent);
        
        return mapToResponseDTO(updatedRide);
    }

    @Transactional
    public RideResponseDTO assignDriver(Long rideId, Long driverId) {
        log.info("Assigning driver {} to ride {}", driverId, rideId);
        
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(rideId));
        
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStateException(
                "Cannot assign driver to ride in status: " + ride.getStatus()
            );
        }

        driverServiceClient.assignDriver(driverId);
        
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ASSIGNED);
        
        Ride updatedRide = rideRepository.save(ride);

        RideEvent rideEvent = RideEvent.builder()
            .rideId(updatedRide.getRideId())
            .userId(updatedRide.getUserId())
            .driverId(updatedRide.getDriverId())
            .status(RideEventStatus.ASSIGNED)
            .message("Driver assigned to ride")
            .build();

        rideProducer.sendRideEvent(rideEvent);
        log.info("Ride {} assigned to driver {} and event published", rideId, driverId);

        return mapToResponseDTO(updatedRide);
    }
    
    @Transactional
    public RideResponseDTO completeRide(Long rideId) {
        return updateRideStatus(rideId, RideStatus.COMPLETED);
    }
    
    /**
     * Helper method to map Ride entity to RideResponseDTO
     */
    private RideResponseDTO mapToResponseDTO(Ride ride) {
        return RideResponseDTO.builder()
            .rideId(ride.getRideId())
            .userId(ride.getUserId())
            .driverId(ride.getDriverId())
            .pickupLocation(ride.getPickupLocation())
            .dropLocation(ride.getDropLocation())
            .status(ride.getStatus())
            .startTime(ride.getStartTime())
            .endTime(ride.getEndTime())
            .build();
    }
    
    /**
     * Validates that the status transition is allowed
     */
    private void validateStatusTransition(RideStatus current, RideStatus next) {
        // Define allowed transitions
        if (current == RideStatus.COMPLETED && next != RideStatus.COMPLETED) {
            throw new InvalidRideStateException(
                "Cannot change status of completed ride"
            );
        }
        
        if (current == RideStatus.CANCELLED) {
            throw new InvalidRideStateException(
                "Cannot change status of cancelled ride"
            );
        }
        
        log.debug("Valid transition from {} to {}", current, next);
    }

    private RideEventStatus toRideEventStatus(RideStatus rideStatus) {
        return RideEventStatus.valueOf(rideStatus.name());
    }
}
