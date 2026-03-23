package com.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideEvent {
    private Long rideId;
    private Long userId;
    private Long driverId;
    private RideEventStatus status;
    private String message;
}
