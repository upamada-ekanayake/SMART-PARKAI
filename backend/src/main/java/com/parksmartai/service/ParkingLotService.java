package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.ParkingLot;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotService {
    private final ParkingLotRepository repository;

    public List<ParkingLotResponse> findAll(String search) {
        List<ParkingLot> lots = search == null || search.isBlank()
                ? repository.findAll()
                : repository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(search, search);
        return lots.stream().map(ParkSmartMapper::toParkingLotResponse).toList();
    }

    public ParkingLot findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));
    }

    public ParkingLotResponse findById(Long id) {
        return ParkSmartMapper.toParkingLotResponse(findEntity(id));
    }

    public ParkingLotResponse create(ParkingLotRequest request) {
        ParkingLot lot = ParkingLot.builder()
                .name(request.name())
                .address(request.address())
                .description(request.description())
                .totalSlots(request.totalSlots())
                .availableSlots(request.availableSlots())
                .build();
        return ParkSmartMapper.toParkingLotResponse(repository.save(lot));
    }

    public ParkingLotResponse update(Long id, ParkingLotRequest request) {
        ParkingLot lot = findEntity(id);
        lot.setName(request.name());
        lot.setAddress(request.address());
        lot.setDescription(request.description());
        lot.setTotalSlots(request.totalSlots());
        lot.setAvailableSlots(request.availableSlots());
        return ParkSmartMapper.toParkingLotResponse(repository.save(lot));
    }

    public ParkingLot save(ParkingLot lot) {
        return repository.save(lot);
    }

    public void delete(Long id) {
        repository.delete(findEntity(id));
    }
}
