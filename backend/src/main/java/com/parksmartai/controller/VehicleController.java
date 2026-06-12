package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService service;

    @GetMapping
    public ApiResponse<List<VehicleResponse>> all(@RequestParam(required = false) String search, @RequestParam(required = false) Long userId) {
        return ApiResponse.ok("Vehicles fetched", userId == null ? service.findAll(search) : service.findByUser(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<VehicleResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("Vehicle fetched", service.findById(id));
    }

    @PostMapping
    public ApiResponse<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        return ApiResponse.ok("Vehicle created", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<VehicleResponse> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return ApiResponse.ok("Vehicle updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Vehicle deleted", null);
    }
}
