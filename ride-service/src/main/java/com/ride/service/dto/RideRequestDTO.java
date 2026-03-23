package com.ride.service.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotBlank(message = "Pickup location is required")
    @Size(min = 5, max = 500, message = "Pickup location must be between 5 and 500 characters")
    private String pickupLocation;
    
    @NotBlank(message = "Drop location is required")
    @Size(min = 5, max = 500, message = "Drop location must be between 5 and 500 characters")
    private String dropLocation;
}
