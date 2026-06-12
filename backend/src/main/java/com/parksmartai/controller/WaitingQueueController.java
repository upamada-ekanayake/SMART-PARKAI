package com.parksmartai.controller;

import com.parksmartai.dto.ApiResponse;
import com.parksmartai.dto.ResourceDtos.*;
import com.parksmartai.service.WaitingQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiting-queue")
@RequiredArgsConstructor
public class WaitingQueueController {
    private final WaitingQueueService service;

    @GetMapping
    public ApiResponse<List<QueueResponse>> all() {
        return ApiResponse.ok("Waiting queue fetched", service.findAll());
    }

    @PostMapping("/join")
    public ApiResponse<QueueResponse> join(@Valid @RequestBody QueueRequest request) {
        return ApiResponse.ok("Joined waiting queue", service.joinQueue(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> leave(@PathVariable Long id) {
        service.leaveQueue(id);
        return ApiResponse.ok("Left waiting queue", null);
    }

    @PostMapping("/assign-next")
    public ApiResponse<QueueResponse> assignNext(@RequestParam Long parkingLotId, @RequestParam Long slotId) {
        return ApiResponse.ok("Next user assigned", service.assignNextUser(parkingLotId, slotId));
    }
}
