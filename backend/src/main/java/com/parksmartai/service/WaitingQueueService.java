package com.parksmartai.service;

import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.entity.User;
import com.parksmartai.entity.WaitingQueueEntry;
import com.parksmartai.enums.SlotStatus;
import com.parksmartai.exception.BadRequestException;
import com.parksmartai.exception.ResourceNotFoundException;
import com.parksmartai.mapper.ParkSmartMapper;
import com.parksmartai.repository.WaitingQueueRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private final WaitingQueueRepository repository;
    private final UserService userService;
    private final ParkingLotService lotService;
    private final ParkingSlotService slotService;
    private final Queue<User> waitingUsers = new LinkedList<>();

    @PostConstruct
    void wireSlotService() {
        slotService.setWaitingQueueService(this);
    }

    public List<QueueResponse> findAll() {
        return repository.findAll().stream().map(ParkSmartMapper::toQueueResponse).toList();
    }

    public QueueResponse joinQueue(QueueRequest request) {
        repository.findByUserIdAndParkingLotId(request.userId(), request.parkingLotId()).ifPresent(entry -> {
            throw new BadRequestException("User is already in this parking lot queue");
        });
        User user = userService.findEntity(request.userId());
        WaitingQueueEntry entry = WaitingQueueEntry.builder()
                .user(user)
                .parkingLot(lotService.findEntity(request.parkingLotId()))
                .notified(false)
                .build();
        waitingUsers.offer(user);
        return ParkSmartMapper.toQueueResponse(repository.save(entry));
    }

    public void leaveQueue(Long id) {
        WaitingQueueEntry entry = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Queue entry not found"));
        waitingUsers.remove(entry.getUser());
        repository.delete(entry);
    }

    @Transactional
    public QueueResponse assignNextUser(Long parkingLotId, Long slotId) {
        List<WaitingQueueEntry> entries = repository.findByParkingLotIdOrderByJoinedAtAsc(parkingLotId);
        if (entries.isEmpty()) {
            return null;
        }
        WaitingQueueEntry next = entries.getFirst();
        next.setNotified(true);
        slotService.markStatus(slotId, SlotStatus.RESERVED);
        waitingUsers.poll();
        return ParkSmartMapper.toQueueResponse(repository.save(next));
    }
}
