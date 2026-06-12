package com.parksmartai.dto;

import com.parksmartai.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserDtos {
    public record UserRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email @NotBlank String email,
            @Size(min = 6) String password,
            String phone,
            Role role,
            boolean vip
    ) {}

    public record UserResponse(Long id, String firstName, String lastName, String email, String phone, Role role, boolean vip, LocalDateTime createdAt) {}
}
