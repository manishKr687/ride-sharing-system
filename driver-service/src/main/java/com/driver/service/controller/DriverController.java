package com.driver.service.controller;

import com.driver.service.entity.Driver;
import com.driver.service.entity.DriverStatus;
import com.driver.service.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/drivers")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver){
        return new ResponseEntity<>(driverService.registerDriver(driver), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Driver>> getAllDrivers(){
        return new ResponseEntity<>(driverService.getAllDrivers(), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> allAvailableDrivers(){
        return new ResponseEntity<>(driverService.getAvailableDrivers(), HttpStatus.OK);
    }

    @PutMapping("/update-status/{driverId}")
    public Driver updateDriverStatus(@PathVariable Long driverId, @RequestParam DriverStatus status) {
        return driverService.updateDriverStatus(driverId, status);
    }

    @PutMapping("/update-location/{driverId}")
    public void updateLocation(@PathVariable Long driverId, @RequestParam Double latitude, @RequestParam Double longitude) {
        driverService.updateDriverLocation(driverId, latitude, longitude);
    }
}
