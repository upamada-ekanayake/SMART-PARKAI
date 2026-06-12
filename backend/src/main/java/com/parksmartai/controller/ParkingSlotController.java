package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.enums.SlotStatus;
import com.parksmartai.service.ParkingSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-slots")
@RequiredArgsConstructor
public class ParkingSlotController {
    private final ParkingSlotService service;

    @GetMapping
    public ApiResponse<List<ParkingSlotResponse>> all(@RequestParam(required = false) Long parkingLotId, @RequestParam(required = false) SlotStatus status) {
        return ApiResponse.ok("Parking slots fetched", service.findAll(parkingLotId, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<ParkingSlotResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("Parking slot fetched", service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ParkingSlotResponse> create(@Valid @RequestBody ParkingSlotRequest request) {
        return ApiResponse.ok("Parking slot created", service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ParkingSlotResponse> update(@PathVariable Long id, @Valid @RequestBody ParkingSlotRequest request) {
        return ApiResponse.ok("Parking slot updated", service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ParkingSlotResponse> status(@PathVariable Long id, @RequestParam SlotStatus status) {
        return ApiResponse.ok("Parking slot status updated", service.markStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Parking slot deleted", null);
    }
}
