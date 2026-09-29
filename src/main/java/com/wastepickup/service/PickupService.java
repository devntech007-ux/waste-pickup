package com.wastepickup.service;

import com.wastepickup.dto.PickupRequest;
import com.wastepickup.entity.Household;
import com.wastepickup.entity.PickupLog;
import com.wastepickup.entity.Schedule;
import com.wastepickup.exception.InvalidPickupWindowException;
import com.wastepickup.repository.HouseholdRepository;
import com.wastepickup.repository.PickupLogRepository;
import com.wastepickup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickupService {

    private final PickupLogRepository logRepo;
    private final HouseholdRepository householdRepo;
    private final ScheduleRepository scheduleRepo;
    private final HouseholdService householdService;

    @Value("${waste.min-score}")
    private double minScore;

    @Value("${waste.rolling-window}")
    private int window;

    @Transactional
    public PickupLog logPickup(PickupRequest r) {
        Household household = householdService.get(r.householdId());
        LocalDateTime when = r.pickupTime() != null ? r.pickupTime() : LocalDateTime.now();

        // BUSINESS RULE 1: pickup must fall inside the zone's scheduled window (checked BEFORE saving)
        List<Schedule> schedulesForDay = scheduleRepo.findByZone_IdAndDayOfWeek(
                household.getZone().getId(), when.getDayOfWeek());
        LocalTime time = when.toLocalTime();
        boolean insideWindow = schedulesForDay.stream()
                .anyMatch(s -> !time.isBefore(s.getStartTime()) && !time.isAfter(s.getEndTime()));
        if (!insideWindow) {
            throw new InvalidPickupWindowException(
                    "Pickup for zone '" + household.getZone().getName() + "' is not allowed on "
                            + when.getDayOfWeek() + " at " + time
                            + ". It must fall inside the scheduled window for that zone.");
        }

        PickupLog entry = new PickupLog();
        entry.setHousehold(household);
        entry.setCollectorName(r.collectorName());
        entry.setScore(r.score());
        entry.setPickupTime(when);
        entry = logRepo.saveAndFlush(entry);

        // Rolling average over the last N pickups
        List<PickupLog> recent = logRepo.findByHouseholdIdOrderByPickupTimeDesc(
                household.getId(), PageRequest.of(0, window));
        double avg = recent.stream().mapToInt(PickupLog::getScore).average().orElse(0);
        household.setRollingAverage(Math.round(avg * 100.0) / 100.0);

        // BUSINESS RULE 2: auto-flag after each pickup
        boolean flagged = avg < minScore;
        household.setFlagged(flagged);
        householdRepo.save(household);

        if (flagged) {
            log.warn("REMINDER NOTICE -> household {} ({}, zone {}): average {} is below minimum {}",
                    household.getId(), household.getOwnerName(), household.getZone().getName(),
                    household.getRollingAverage(), minScore);
        }
        return entry;
    }

    @Transactional(readOnly = true)
    public List<PickupLog> historyOf(Long householdId) {
        householdService.get(householdId);
        return logRepo.findByHouseholdIdOrderByPickupTimeDesc(householdId);
    }
}
