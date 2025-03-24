package com.driver.service.entity;

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
    private String name;
    private String phoneNumber;
    private String vehicleNumber;
    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private Double currentLatitude;
    private Double currentLongitude;
}
