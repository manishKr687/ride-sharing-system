package com.location.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyDriverDTO {
    private Long driverId;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
}
