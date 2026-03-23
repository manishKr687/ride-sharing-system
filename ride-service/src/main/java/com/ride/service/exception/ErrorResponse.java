package com.ride.service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for all API errors.
 * Provides consistent error format across the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Only include non-null fields in JSON
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Error type/category
     */
    private String error;
    
    /**
     * Human-readable error message
     */
    private String message;
    
    /**
     * Request path where error occurred
     */
    private String path;
    
    /**
     * Additional error details (e.g., validation errors)
     */
    private Map<String, String> details;
}
