package com.driver.service.repository;

import com.driver.service.entity.Driver;
import com.driver.service.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    List<Driver> findByDriverStatus(DriverStatus driverStatus);
}
