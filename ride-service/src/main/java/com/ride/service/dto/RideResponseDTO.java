package com.ride.service.dto;

import com.ride.service.entity.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDTO {
    private Long rideId;
    private Long userId;
    private Long driverId;
    private String pickupLocation;
    private String dropLocation;
    private RideStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
