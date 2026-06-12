package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.ParkingLot;
import com.parksmartai.entity.ParkingSlot;
import com.parksmartai.enums.SlotStatus;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSlotService {
    private final ParkingSlotRepository repository;
    private final ParkingLotService lotService;
    private WaitingQueueService waitingQueueService;
    private final HashMap<Long, ParkingSlot> slotLookup = new HashMap<>();

    public void setWaitingQueueService(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    public List<ParkingSlotResponse> findAll(Long parkingLotId, SlotStatus status) {
        List<ParkingSlot> slots = parkingLotId != null ? repository.findByParkingLotId(parkingLotId) : repository.findAll();
        return slots.stream()
                .filter(slot -> status == null || slot.getStatus() == status)
                .peek(slot -> slotLookup.put(slot.getId(), slot))
                .map(ParkSmartMapper::toParkingSlotResponse)
                .toList();
    }

    public ParkingSlot findEntity(Long id) {
        ParkingSlot cached = slotLookup.get(id);
        if (cached != null) {
            return cached;
        }
        ParkingSlot slot = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Parking slot not found"));
        slotLookup.put(id, slot);
        return slot;
    }

    public ParkingSlotResponse findById(Long id) {
        return ParkSmartMapper.toParkingSlotResponse(findEntity(id));
    }

    public ParkingSlotResponse create(ParkingSlotRequest request) {
        ParkingLot lot = lotService.findEntity(request.parkingLotId());
        ParkingSlot slot = ParkingSlot.builder()
                .slotNumber(request.slotNumber())
                .slotType(request.slotType())
                .status(request.status())
                .parkingLot(lot)
                .build();
        lot.setTotalSlots(lot.getTotalSlots() + 1);
        if (request.status() == SlotStatus.AVAILABLE) {
            lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        }
        lotService.save(lot);
        ParkingSlot saved = repository.save(slot);
        slotLookup.put(saved.getId(), saved);
        return ParkSmartMapper.toParkingSlotResponse(saved);
    }

    @Transactional
    public ParkingSlotResponse update(Long id, ParkingSlotRequest request) {
        ParkingSlot slot = findEntity(id);
        SlotStatus oldStatus = slot.getStatus();
        ParkingLot lot = lotService.findEntity(request.parkingLotId());
        slot.setSlotNumber(request.slotNumber());
        slot.setSlotType(request.slotType());
        slot.setStatus(request.status());
        slot.setParkingLot(lot);
        adjustAvailability(lot, oldStatus, request.status());
        ParkingSlot saved = repository.save(slot);
        slotLookup.put(saved.getId(), saved);
        if (oldStatus != SlotStatus.AVAILABLE && request.status() == SlotStatus.AVAILABLE && waitingQueueService != null) {
            waitingQueueService.assignNextUser(lot.getId(), saved.getId());
        }
        return ParkSmartMapper.toParkingSlotResponse(saved);
    }

    public ParkingSlotResponse markStatus(Long id, SlotStatus status) {
        ParkingSlot slot = findEntity(id);
        return update(id, new ParkingSlotRequest(slot.getSlotNumber(), slot.getSlotType(), status, slot.getParkingLot().getId()));
    }

    public void delete(Long id) {
        ParkingSlot slot = findEntity(id);
        ParkingLot lot = slot.getParkingLot();
        lot.setTotalSlots(Math.max(0, lot.getTotalSlots() - 1));
        if (slot.getStatus() == SlotStatus.AVAILABLE) {
            lot.setAvailableSlots(Math.max(0, lot.getAvailableSlots() - 1));
        }
        lotService.save(lot);
        slotLookup.remove(id);
        repository.delete(slot);
    }

    public long totalSlots() {
        return repository.count();
    }

    public long occupiedOrReservedSlots() {
        return repository.countByStatus(SlotStatus.OCCUPIED) + repository.countByStatus(SlotStatus.RESERVED);
    }

    private void adjustAvailability(ParkingLot lot, SlotStatus oldStatus, SlotStatus newStatus) {
        if (oldStatus != SlotStatus.AVAILABLE && newStatus == SlotStatus.AVAILABLE) {
            lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        }
        if (oldStatus == SlotStatus.AVAILABLE && newStatus != SlotStatus.AVAILABLE) {
            lot.setAvailableSlots(Math.max(0, lot.getAvailableSlots() - 1));
        }
        lotService.save(lot);
    }
}
