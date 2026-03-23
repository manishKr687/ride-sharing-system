package com.location.service.repository;

import com.location.service.entity.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
    Optional<DriverLocation> findByDriverId(Long driverId);
    List<DriverLocation> findAllByLatitudeNotNullAndLongitudeNotNull();
}
