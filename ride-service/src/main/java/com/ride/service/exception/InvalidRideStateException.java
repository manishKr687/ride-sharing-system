package com.ride.service.exception;

public class InvalidRideStateException extends RuntimeException{
        public InvalidRideStateException(String message) {
            super(message);
    }
}
