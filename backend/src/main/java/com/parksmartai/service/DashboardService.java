package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.DashboardStats;
import com.parksmartai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingLotRepository lotRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingSlotService slotService;

    public DashboardStats stats() {
        long totalSlots = slotService.totalSlots();
        double occupancy = totalSlots == 0 ? 0 : Math.round(((double) slotService.occupiedOrReservedSlots() / totalSlots) * 10000.0) / 100.0;
        return new DashboardStats(
                userRepository.count(),
                vehicleRepository.count(),
                lotRepository.count(),
                totalSlots,
                bookingRepository.count(),
                paymentRepository.totalRevenue(),
                occupancy);
    }
}
