package com.wastepickup.controller;

import com.wastepickup.dto.ScheduleRequest;
import com.wastepickup.entity.Schedule;
import com.wastepickup.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public Schedule get(@PathVariable Long id) {
        return scheduleService.get(id);
    }

    @PutMapping("/{id}")
    public Schedule update(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        return scheduleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        scheduleService.delete(id);
    }
}
