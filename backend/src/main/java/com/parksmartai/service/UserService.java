package com.parksmartai.service;

import com.parksmartai.dto.UserDtos.*;
import com.parksmartai.entity.User;
import com.parksmartai.enums.Role;
import com.parksmartai.exception.BadRequestException;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(ParkSmartMapper::toUserResponse).toList();
    }

    public User findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse findById(Long id) {
        return ParkSmartMapper.toUserResponse(findEntity(id));
    }

    public UserResponse create(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .role(request.role() == null ? Role.ROLE_USER : request.role())
                .vip(request.vip())
                .build();
        return ParkSmartMapper.toUserResponse(repository.save(user));
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = findEntity(id);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole(request.role() == null ? user.getRole() : request.role());
        user.setVip(request.vip());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        return ParkSmartMapper.toUserResponse(repository.save(user));
    }

    public void delete(Long id) {
        repository.delete(findEntity(id));
    }
}
