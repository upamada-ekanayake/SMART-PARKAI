package com.parksmartai.repository;

import com.parksmartai.entity.ParkingSlot;
import com.parksmartai.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByParkingLotId(Long parkingLotId);
    List<ParkingSlot> findByStatus(SlotStatus status);
    Optional<ParkingSlot> findFirstByParkingLotIdAndStatusOrderBySlotNumberAsc(Long parkingLotId, SlotStatus status);
    long countByStatus(SlotStatus status);
}
