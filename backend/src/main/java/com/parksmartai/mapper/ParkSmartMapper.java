package com.parksmartai.mapper;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.dto.UserDtos.UserResponse;
import com.parksmartai.entity.*;

public final class ParkSmartMapper {
    private ParkSmartMapper() {}

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getRole(), user.isVip(), user.getCreatedAt());
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        User user = vehicle.getUser();
        return new VehicleResponse(vehicle.getId(), user.getId(), user.getFirstName() + " " + user.getLastName(), vehicle.getVehicleNumber(), vehicle.getVehicleType(), vehicle.getBrand(), vehicle.getModel(), vehicle.getColor());
    }

    public static ParkingLotResponse toParkingLotResponse(ParkingLot lot) {
        return new ParkingLotResponse(lot.getId(), lot.getName(), lot.getAddress(), lot.getDescription(), lot.getTotalSlots(), lot.getAvailableSlots());
    }

    public static ParkingSlotResponse toParkingSlotResponse(ParkingSlot slot) {
        return new ParkingSlotResponse(slot.getId(), slot.getSlotNumber(), slot.getSlotType(), slot.getStatus(), slot.getParkingLot().getId(), slot.getParkingLot().getName());
    }

    public static BookingResponse toBookingResponse(Booking booking) {
        User user = booking.getUser();
        Vehicle vehicle = booking.getVehicle();
        ParkingSlot slot = booking.getSlot();
        return new BookingResponse(booking.getId(), user.getId(), user.getFirstName() + " " + user.getLastName(), vehicle.getId(), vehicle.getVehicleNumber(), slot.getId(), slot.getSlotNumber(), booking.getBookingDate(), booking.getStartTime(), booking.getEndTime(), booking.getBookingStatus(), booking.getQrCodeBase64(), booking.getCreatedAt());
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getBooking().getId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentStatus(), payment.getPaymentDate());
    }

    public static QueueResponse toQueueResponse(WaitingQueueEntry entry) {
        User user = entry.getUser();
        ParkingLot lot = entry.getParkingLot();
        return new QueueResponse(entry.getId(), user.getId(), user.getFirstName() + " " + user.getLastName(), lot.getId(), lot.getName(), entry.getJoinedAt(), entry.isNotified());
    }
}
