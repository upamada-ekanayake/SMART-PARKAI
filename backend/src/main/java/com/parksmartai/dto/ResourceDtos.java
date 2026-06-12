package com.parksmartai.dto;

import com.parksmartai.enums.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResourceDtos {
    public record VehicleRequest(@NotNull Long userId, @NotBlank String vehicleNumber, @NotNull VehicleType vehicleType, String brand, String model, String color) {}
    public record VehicleResponse(Long id, Long userId, String ownerName, String vehicleNumber, VehicleType vehicleType, String brand, String model, String color) {}

    public record ParkingLotRequest(@NotBlank String name, @NotBlank String address, String description, @PositiveOrZero int totalSlots, @PositiveOrZero int availableSlots) {}
    public record ParkingLotResponse(Long id, String name, String address, String description, int totalSlots, int availableSlots) {}

    public record ParkingSlotRequest(@NotBlank String slotNumber, @NotBlank String slotType, @NotNull SlotStatus status, @NotNull Long parkingLotId) {}
    public record ParkingSlotResponse(Long id, String slotNumber, String slotType, SlotStatus status, Long parkingLotId, String parkingLotName) {}

    public record BookingRequest(@NotNull Long userId, @NotNull Long vehicleId, @NotNull Long slotId, @NotNull LocalDate bookingDate, @NotNull LocalTime startTime, @NotNull LocalTime endTime, BookingStatus bookingStatus) {}
    public record BookingResponse(Long id, Long userId, String userName, Long vehicleId, String vehicleNumber, Long slotId, String slotNumber, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, BookingStatus bookingStatus, String qrCodeBase64, LocalDateTime createdAt) {}

    public record PaymentRequest(@NotNull Long bookingId, BigDecimal amount, @NotNull PaymentMethod paymentMethod, PaymentStatus paymentStatus) {}
    public record PaymentResponse(Long id, Long bookingId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime paymentDate) {}

    public record QueueRequest(@NotNull Long userId, @NotNull Long parkingLotId) {}
    public record QueueResponse(Long id, Long userId, String userName, Long parkingLotId, String parkingLotName, LocalDateTime joinedAt, boolean notified) {}

    public record DashboardStats(long totalUsers, long totalVehicles, long totalParkingLots, long totalSlots, long totalBookings, BigDecimal revenue, double occupancyRate) {}
}
