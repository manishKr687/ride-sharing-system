package com.ride.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    @Enumerated(EnumType.STRING)
    private RideStatus status;

}
