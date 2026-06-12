package com.parksmartai.repository;

import com.parksmartai.entity.Booking;
import com.parksmartai.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByBookingDate(LocalDate date);
    List<Booking> findByBookingStatus(BookingStatus status);
    List<Booking> findByBookingDateAndBookingStatus(LocalDate date, BookingStatus status);
}
