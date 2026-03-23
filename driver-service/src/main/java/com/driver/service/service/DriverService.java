package com.driver.service.service;

import com.driver.service.dto.DriverRequestDTO;
import com.driver.service.dto.DriverResponseDTO;
import com.driver.service.entity.Driver;
import com.driver.service.entity.DriverStatus;
import com.driver.service.exception.DriverNotFoundException;
import com.driver.service.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverResponseDTO registerDriver(DriverRequestDTO request){
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setVehicleNumber(request.getVehicleNumber());
        driver.setDriverStatus(request.getDriverStatus());
        driver.setCurrentLatitude(request.getCurrentLatitude());
        driver.setCurrentLongitude(request.getCurrentLongitude());
        return mapToResponse(driverRepository.save(driver));
    }

    public List<DriverResponseDTO> getAllDrivers(){
        return driverRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    public List<DriverResponseDTO> getAvailableDrivers(){
        return driverRepository.findByDriverStatus(DriverStatus.AVAILABLE).stream()
            .map(this::mapToResponse)
            .toList();
    }

    public DriverResponseDTO updateDriverStatus(Long driverId, DriverStatus driverStatus){
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(driverId));
        driver.setDriverStatus(driverStatus);
        return mapToResponse(driverRepository.save(driver));
    }

    public void updateDriverLocation(Long driverId, Double latitude, Double longitude){
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(driverId));
        driver.setCurrentLongitude(longitude);;
        driver.setCurrentLatitude(latitude);
        driverRepository.save(driver);
    }

    private DriverResponseDTO mapToResponse(Driver driver) {
        return DriverResponseDTO.builder()
            .driverId(driver.getDriverId())
            .name(driver.getName())
            .phoneNumber(driver.getPhoneNumber())
            .vehicleNumber(driver.getVehicleNumber())
            .driverStatus(driver.getDriverStatus())
            .currentLatitude(driver.getCurrentLatitude())
            .currentLongitude(driver.getCurrentLongitude())
            .build();
    }

}
