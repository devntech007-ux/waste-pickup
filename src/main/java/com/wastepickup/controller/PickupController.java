package com.wastepickup.controller;

import com.wastepickup.dto.PickupRequest;
import com.wastepickup.entity.PickupLog;
import com.wastepickup.service.PickupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pickups")
@RequiredArgsConstructor
public class PickupController {

    private final PickupService pickupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PickupLog logPickup(@Valid @RequestBody PickupRequest request) {
        return pickupService.logPickup(request);
    }
}
