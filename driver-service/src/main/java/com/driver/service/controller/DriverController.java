package com.driver.service.controller;

import com.driver.service.dto.DriverRequestDTO;
import com.driver.service.dto.DriverResponseDTO;
import com.driver.service.entity.Driver;
import com.driver.service.entity.DriverStatus;
import com.driver.service.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<DriverResponseDTO> registerDriver(@RequestBody DriverRequestDTO driver){
        return new ResponseEntity<>(driverService.registerDriver(driver), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers(){
        return new ResponseEntity<>(driverService.getAllDrivers(), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverResponseDTO>> allAvailableDrivers(){
        return new ResponseEntity<>(driverService.getAvailableDrivers(), HttpStatus.OK);
    }

    @PutMapping("/update-status/{driverId}")
    public DriverResponseDTO updateDriverStatus(@PathVariable Long driverId, @RequestParam DriverStatus status) {
        return driverService.updateDriverStatus(driverId, status);
    }

    @PutMapping("/update-location/{driverId}")
    public void updateLocation(@PathVariable Long driverId, @RequestParam Double latitude, @RequestParam Double longitude) {
        driverService.updateDriverLocation(driverId, latitude, longitude);
    }
}
