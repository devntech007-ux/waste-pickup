package com.wastepickup.controller;

import com.wastepickup.dto.HouseholdRequest;
import com.wastepickup.entity.Household;
import com.wastepickup.entity.PickupLog;
import com.wastepickup.service.HouseholdService;
import com.wastepickup.service.PickupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;
    private final PickupService pickupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Household create(@Valid @RequestBody HouseholdRequest request) {
        return householdService.create(request);
    }

    @GetMapping
    public List<Household> getAll() {
        return householdService.getAll();
    }

    /** Households below the minimum average score (need a reminder notice). */
    @GetMapping("/flagged")
    public List<Household> getFlagged() {
        return householdService.getFlagged();
    }

    @GetMapping("/{id}")
    public Household get(@PathVariable Long id) {
        return householdService.get(id);
    }

    @PutMapping("/{id}")
    public Household update(@PathVariable Long id, @Valid @RequestBody HouseholdRequest request) {
        return householdService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        householdService.delete(id);
    }

    @GetMapping("/{id}/pickups")
    public List<PickupLog> history(@PathVariable Long id) {
        return pickupService.historyOf(id);
    }
}
