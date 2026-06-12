package com.parksmartai.repository;

import com.parksmartai.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByVehicleNumber(String vehicleNumber);
    List<Vehicle> findByUserId(Long userId);
    List<Vehicle> findByVehicleNumberContainingIgnoreCaseOrBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String number, String brand, String model);
}
