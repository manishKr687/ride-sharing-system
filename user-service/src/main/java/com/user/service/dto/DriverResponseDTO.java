package com.user.service.dto;

import com.user.service.entity.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponseDTO {
    private Long driverId;
    private Long userId;
    private String name;
    private String phoneNumber;
    private String vehicleNumber;
    private DriverStatus driverStatus;
    private Double currentLatitude;
    private Double currentLongitude;
}
