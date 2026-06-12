package com.parksmartai.repository;

import com.parksmartai.entity.WaitingQueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository extends JpaRepository<WaitingQueueEntry, Long> {
    List<WaitingQueueEntry> findByParkingLotIdOrderByJoinedAtAsc(Long parkingLotId);
    Optional<WaitingQueueEntry> findByUserIdAndParkingLotId(Long userId, Long parkingLotId);
}
