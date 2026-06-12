package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.enums.PaymentStatus;
import com.parksmartai.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @GetMapping
    public ApiResponse<List<PaymentResponse>> all(@RequestParam(required = false) PaymentStatus status) {
        return ApiResponse.ok("Payments fetched", service.findAll(status));
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("Payment fetched", service.findById(id));
    }

    @PostMapping
    public ApiResponse<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment created", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PaymentResponse> update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Payment deleted", null);
    }
}
