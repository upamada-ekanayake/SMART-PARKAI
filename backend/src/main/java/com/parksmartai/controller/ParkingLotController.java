package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.service.ParkingLotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {
    private final ParkingLotService service;

    @GetMapping
    public ApiResponse<List<ParkingLotResponse>> all(@RequestParam(required = false) String search) {
        return ApiResponse.ok("Parking lots fetched", service.findAll(search));
    }

    @GetMapping("/{id}")
    public ApiResponse<ParkingLotResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("Parking lot fetched", service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ParkingLotResponse> create(@Valid @RequestBody ParkingLotRequest request) {
        return ApiResponse.ok("Parking lot created", service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ParkingLotResponse> update(@PathVariable Long id, @Valid @RequestBody ParkingLotRequest request) {
        return ApiResponse.ok("Parking lot updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Parking lot deleted", null);
    }
}
