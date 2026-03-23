package com.user.service.exception;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Long id) {
        super("Driver not found with id: " + id);
    }
}
