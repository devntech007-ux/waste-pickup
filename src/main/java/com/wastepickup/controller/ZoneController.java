package com.wastepickup.controller;

import com.wastepickup.dto.ScheduleRequest;
import com.wastepickup.dto.ZoneAverageResponse;
import com.wastepickup.dto.ZoneRequest;
import com.wastepickup.entity.Schedule;
import com.wastepickup.entity.Zone;
import com.wastepickup.service.ScheduleService;
import com.wastepickup.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;
    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Zone create(@Valid @RequestBody ZoneRequest request) {
        return zoneService.create(request);
    }

    @GetMapping
    public List<Zone> getAll() {
        return zoneService.getAll();
    }

    @GetMapping("/{id}")
    public Zone get(@PathVariable Long id) {
        return zoneService.get(id);
    }

    @PutMapping("/{id}")
    public Zone update(@PathVariable Long id, @Valid @RequestBody ZoneRequest request) {
        return zoneService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        zoneService.delete(id);
    }

    // ---- schedules of a zone ----
    @PostMapping("/{id}/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public Schedule addSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        return scheduleService.create(id, request);
    }

    @GetMapping("/{id}/schedules")
    public List<Schedule> getSchedules(@PathVariable Long id) {
        return scheduleService.getByZone(id);
    }

    // ---- zone-wise average segregation score ----
    @GetMapping("/average-scores")
    public List<ZoneAverageResponse> allAverages() {
        return zoneService.allAverages();
    }

    @GetMapping("/{id}/average-score")
    public ZoneAverageResponse average(@PathVariable Long id) {
        return zoneService.averageOf(id);
    }
}
