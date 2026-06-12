package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.DashboardStats;
import com.parksmartai.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DashboardService service;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStats> dashboard() {
        return ApiResponse.ok("Dashboard analytics fetched", service.stats());
    }
}
