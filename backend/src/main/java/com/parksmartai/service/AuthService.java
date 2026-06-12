package com.parksmartai.service;

import com.parksmartai.dto.AuthDtos.*;
import com.parksmartai.entity.User;
import com.parksmartai.enums.Role;
import com.parksmartai.exception.BadRequestException;
import com.parksmartai.repository.UserRepository;
import com.parksmartai.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.email());
        try {
            if (userRepository.existsByEmail(request.email())) {
                log.warn("Registration failed: Email already exists - {}", request.email());
                throw new BadRequestException("Email already exists");
            }
            if (request.password() == null || request.password().length() < 6) {
                throw new BadRequestException("Password must be at least 6 characters long");
            }
            User user = User.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .phone(request.phone())
                    .role(Role.ROLE_USER)
                    .vip(false)
                    .build();
            userRepository.save(user);
            log.info("User registered successfully: {}", request.email());
            return response(user);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Registration error for email: {}", request.email(), e);
            throw new BadRequestException("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Attempting to login user: {}", request.email());
        try {
            if (request.email() == null || request.email().trim().isEmpty()) {
                throw new BadCredentialsException("Email cannot be empty");
            }
            if (request.password() == null || request.password().isEmpty()) {
                throw new BadCredentialsException("Password cannot be empty");
            }
            
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            log.info("User logged in successfully: {}", request.email());
            return response(user);
        } catch (BadCredentialsException e) {
            log.warn("Login failed for user: {} - {}", request.email(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Login error for user: {}", request.email(), e);
            throw new BadCredentialsException("Login failed: " + e.getMessage());
        }
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.isVip());
    }
}
