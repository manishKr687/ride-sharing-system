package com.ride.service.dto;

import com.ride.service.entity.RideStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideStatusUpdateDTO {
    
    @NotNull(message = "Status is required")
    private RideStatus status;
}
