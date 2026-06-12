package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.enums.BookingStatus;
import com.parksmartai.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @GetMapping
    public ApiResponse<List<BookingResponse>> all(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) BookingStatus status, @RequestParam(required = false) Long userId) {
        return ApiResponse.ok("Bookings fetched", userId == null ? service.findAll(date, status) : service.findByUser(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("Booking fetched", service.findById(id));
    }

    @PostMapping
    public ApiResponse<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        return ApiResponse.ok("Booking created", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        return ApiResponse.ok("Booking updated", service.update(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok("Booking cancelled", service.cancel(id));
    }

    @GetMapping("/{id}/verify")
    public ApiResponse<Map<String, Boolean>> verify(@PathVariable Long id) {
        return ApiResponse.ok("Booking verification completed", Map.of("valid", service.verifyQr(id)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Booking deleted", null);
    }
}
