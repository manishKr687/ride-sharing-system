package com.user.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    @Column(unique = true, nullable = false)
    private Long userId;

    private String name;
    private String phoneNumber;
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private Double currentLatitude;
    private Double currentLongitude;
}
