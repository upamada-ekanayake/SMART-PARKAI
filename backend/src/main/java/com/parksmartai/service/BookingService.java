package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.Booking;
import com.parksmartai.entity.ParkingSlot;
import com.parksmartai.entity.User;
import com.parksmartai.enums.BookingStatus;
import com.parksmartai.enums.SlotStatus;
import com.parksmartai.exception.BadRequestException;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.BookingRepository;
import com.parksmartai.util.QrCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final ParkingSlotService slotService;
    private final QrCodeUtil qrCodeUtil;
    private final TreeMap<LocalDate, List<Booking>> bookingsByDate = new TreeMap<>();
    private final PriorityQueue<Booking> vipPriorityQueue = new PriorityQueue<>(
            Comparator.<Booking, Boolean>comparing(b -> !b.getUser().isVip())
                    .thenComparing(Booking::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())));

    public List<BookingResponse> findAll(LocalDate date, BookingStatus status) {
        List<Booking> bookings;
        if (date != null && status != null) {
            bookings = repository.findByBookingDateAndBookingStatus(date, status);
        } else if (date != null) {
            bookings = repository.findByBookingDate(date);
        } else if (status != null) {
            bookings = repository.findByBookingStatus(status);
        } else {
            bookings = repository.findAll();
        }
        bookingsByDate.put(date == null ? LocalDate.now() : date, bookings);
        return bookings.stream().map(ParkSmartMapper::toBookingResponse).toList();
    }

    public List<BookingResponse> findByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(ParkSmartMapper::toBookingResponse).toList();
    }

    public Booking findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public BookingResponse findById(Long id) {
        return ParkSmartMapper.toBookingResponse(findEntity(id));
    }

    @Transactional
    public BookingResponse create(BookingRequest request) {
        ParkingSlot slot = slotService.findEntity(request.slotId());
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new BadRequestException("Selected slot is not available. Join the waiting queue instead.");
        }
        User user = userService.findEntity(request.userId());
        Booking booking = Booking.builder()
                .user(user)
                .vehicle(vehicleService.findEntity(request.vehicleId()))
                .slot(slot)
                .bookingDate(request.bookingDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .bookingStatus(request.bookingStatus() == null ? BookingStatus.CONFIRMED : request.bookingStatus())
                .build();
        Booking saved = repository.save(booking);
        saved.setQrCodeBase64(qrCodeUtil.generateBase64("BOOKING:" + saved.getId() + "|USER:" + user.getId() + "|SLOT:" + slot.getId()));
        slotService.markStatus(slot.getId(), SlotStatus.RESERVED);
        Booking finalBooking = repository.save(saved);
        vipPriorityQueue.offer(finalBooking);
        bookingsByDate.put(finalBooking.getBookingDate(), repository.findByBookingDate(finalBooking.getBookingDate()));
        return ParkSmartMapper.toBookingResponse(finalBooking);
    }

    @Transactional
    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = findEntity(id);
        booking.setUser(userService.findEntity(request.userId()));
        booking.setVehicle(vehicleService.findEntity(request.vehicleId()));
        booking.setSlot(slotService.findEntity(request.slotId()));
        booking.setBookingDate(request.bookingDate());
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setBookingStatus(request.bookingStatus() == null ? booking.getBookingStatus() : request.bookingStatus());
        Booking saved = repository.save(booking);
        bookingsByDate.put(saved.getBookingDate(), repository.findByBookingDate(saved.getBookingDate()));
        return ParkSmartMapper.toBookingResponse(saved);
    }

    @Transactional
    public BookingResponse cancel(Long id) {
        Booking booking = findEntity(id);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        slotService.markStatus(booking.getSlot().getId(), SlotStatus.AVAILABLE);
        return ParkSmartMapper.toBookingResponse(repository.save(booking));
    }

    public boolean verifyQr(Long bookingId) {
        return repository.existsById(bookingId);
    }

    public void delete(Long id) {
        repository.delete(findEntity(id));
    }
}
