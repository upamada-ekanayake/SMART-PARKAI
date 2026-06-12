package com.parksmartai.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PricingServiceTest {
    @Test
    void appliesPeakWeekendAndOccupancySurcharges() {
        ParkingSlotService slotService = mock(ParkingSlotService.class);
        when(slotService.totalSlots()).thenReturn(10L);
        when(slotService.occupiedOrReservedSlots()).thenReturn(9L);
        PricingService service = new PricingService(slotService);

        BigDecimal amount = service.calculate(LocalDate.of(2026, 6, 13), LocalTime.of(8, 30));

        assertThat(amount).isEqualByComparingTo("200");
    }
}
