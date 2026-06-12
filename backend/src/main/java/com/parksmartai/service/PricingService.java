package com.parksmartai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final ParkingSlotService slotService;

    public BigDecimal calculate(LocalDate date, LocalTime startTime) {
        int amount = 100;
        boolean peakHour = !startTime.isBefore(LocalTime.of(7, 0)) && startTime.isBefore(LocalTime.of(10, 0))
                || !startTime.isBefore(LocalTime.of(16, 0)) && startTime.isBefore(LocalTime.of(19, 0));
        if (peakHour) {
            amount += 50;
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            amount += 25;
        }
        long total = slotService.totalSlots();
        if (total > 0 && ((double) slotService.occupiedOrReservedSlots() / total) > 0.8) {
            amount += 25;
        }
        return BigDecimal.valueOf(amount);
    }
}
