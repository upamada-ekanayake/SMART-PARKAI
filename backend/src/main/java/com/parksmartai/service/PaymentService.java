package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.Booking;
import com.parksmartai.entity.Payment;
import com.parksmartai.enums.PaymentStatus;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final BookingService bookingService;
    private final PricingService pricingService;

    public List<PaymentResponse> findAll(PaymentStatus status) {
        List<Payment> payments = status == null ? repository.findAll() : repository.findByPaymentStatus(status);
        return payments.stream().map(ParkSmartMapper::toPaymentResponse).toList();
    }

    public Payment findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    public PaymentResponse findById(Long id) {
        return ParkSmartMapper.toPaymentResponse(findEntity(id));
    }

    public PaymentResponse create(PaymentRequest request) {
        Booking booking = bookingService.findEntity(request.bookingId());
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.amount() == null ? pricingService.calculate(booking.getBookingDate(), booking.getStartTime()) : request.amount())
                .paymentMethod(request.paymentMethod())
                .paymentStatus(request.paymentStatus() == null ? PaymentStatus.PAID : request.paymentStatus())
                .paymentDate(LocalDateTime.now())
                .build();
        return ParkSmartMapper.toPaymentResponse(repository.save(payment));
    }

    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = findEntity(id);
        Booking booking = bookingService.findEntity(request.bookingId());
        payment.setBooking(booking);
        payment.setAmount(request.amount() == null ? pricingService.calculate(booking.getBookingDate(), booking.getStartTime()) : request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(request.paymentStatus() == null ? payment.getPaymentStatus() : request.paymentStatus());
        return ParkSmartMapper.toPaymentResponse(repository.save(payment));
    }

    public void delete(Long id) {
        repository.delete(findEntity(id));
    }
}
