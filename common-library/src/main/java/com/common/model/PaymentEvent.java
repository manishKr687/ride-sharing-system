package com.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {
    private Long paymentId;
    private String userId;
    private String rideId;
    private String status;
    private Double amount;
    private String message;
}

