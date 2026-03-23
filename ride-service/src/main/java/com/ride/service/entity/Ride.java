package com.ride.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = true)
    private Long driverId;
    @Column(nullable = false, length = 500)
    private String pickupLocation;
    @Column(nullable = false, length = 500)
    private String dropLocation;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    private RideStatus status;

}
