package com.driver.service.service;

import com.driver.service.entity.Driver;
import com.driver.service.entity.DriverStatus;
import com.driver.service.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public Driver registerDriver(Driver driver){
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers(){
        return driverRepository.findAll();
    }

    public List<Driver> getAvailableDrivers(){
        return driverRepository.findByDriverStatus(DriverStatus.AVAILABLE);
    }

    public Driver updateDriverStatus(Long driverId, DriverStatus driverStatus){
        Driver driver = driverRepository.findById(driverId).orElseThrow(()-> new RuntimeException("Driver Not found"));
        driver.setDriverStatus(driverStatus);
        return driverRepository.save(driver);
    }

    public void updateDriverLocation(Long driverId, Double latitude, Double longitude){
        Driver driver = driverRepository.findById(driverId).orElseThrow(()-> new RuntimeException("Driver Not found"));
        driver.setCurrentLongitude(longitude);;
        driver.setCurrentLatitude(latitude);
        driverRepository.save(driver);
    }

}
