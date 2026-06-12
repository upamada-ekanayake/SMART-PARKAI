package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.UserDtos.*;
import com.parksmartai.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> all() {
        return ApiResponse.ok("Users fetched", service.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> one(@PathVariable Long id) {
        return ApiResponse.ok("User fetched", service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ApiResponse.ok("User created", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ApiResponse.ok("User updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("User deleted", null);
    }
}
