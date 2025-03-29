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
    private String userId;
    private String driverId;
    private String status;  // REQUESTED, ACCEPTED, COMPLETED
    private String message;
}
