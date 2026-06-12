package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.Vehicle;
import com.parksmartai.exception.BadRequestException;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository repository;
    private final UserService userService;

    public List<VehicleResponse> findAll(String search) {
        List<Vehicle> vehicles = search == null || search.isBlank()
                ? repository.findAll()
                : repository.findByVehicleNumberContainingIgnoreCaseOrBrandContainingIgnoreCaseOrModelContainingIgnoreCase(search, search, search);
        return vehicles.stream().map(ParkSmartMapper::toVehicleResponse).toList();
    }

    public List<VehicleResponse> findByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(ParkSmartMapper::toVehicleResponse).toList();
    }

    public Vehicle findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    public VehicleResponse findById(Long id) {
        return ParkSmartMapper.toVehicleResponse(findEntity(id));
    }

    public VehicleResponse create(VehicleRequest request) {
        if (repository.existsByVehicleNumber(request.vehicleNumber())) {
            throw new BadRequestException("Vehicle number already exists");
        }
        Vehicle vehicle = Vehicle.builder()
                .user(userService.findEntity(request.userId()))
                .vehicleNumber(request.vehicleNumber())
                .vehicleType(request.vehicleType())
                .brand(request.brand())
                .model(request.model())
                .color(request.color())
                .build();
        return ParkSmartMapper.toVehicleResponse(repository.save(vehicle));
    }

    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = findEntity(id);
        vehicle.setUser(userService.findEntity(request.userId()));
        vehicle.setVehicleNumber(request.vehicleNumber());
        vehicle.setVehicleType(request.vehicleType());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setColor(request.color());
        return ParkSmartMapper.toVehicleResponse(repository.save(vehicle));
    }

    public void delete(Long id) {
        repository.delete(findEntity(id));
    }
}
