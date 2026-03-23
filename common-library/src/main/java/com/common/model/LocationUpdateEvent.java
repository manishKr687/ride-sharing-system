package com.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationUpdateEvent {
    private Long driverId;
    private Long userId;
    private Double latitude;
    private Double longitude;
}
