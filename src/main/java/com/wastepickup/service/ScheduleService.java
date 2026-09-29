package com.wastepickup.service;

import com.wastepickup.dto.ScheduleRequest;
import com.wastepickup.entity.Schedule;
import com.wastepickup.exception.BusinessRuleException;
import com.wastepickup.exception.ResourceNotFoundException;
import com.wastepickup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepo;
    private final ZoneService zoneService;

    @Transactional
    public Schedule create(Long zoneId, ScheduleRequest r) {
        validateTimes(r);
        Schedule s = new Schedule();
        s.setZone(zoneService.get(zoneId));
        apply(s, r);
        return scheduleRepo.save(s);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getByZone(Long zoneId) {
        zoneService.get(zoneId);
        return scheduleRepo.findByZone_Id(zoneId);
    }

    @Transactional(readOnly = true)
    public Schedule get(Long id) {
        return scheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + id));
    }

    @Transactional
    public Schedule update(Long id, ScheduleRequest r) {
        validateTimes(r);
        Schedule s = get(id);
        apply(s, r);
        return scheduleRepo.save(s);
    }

    @Transactional
    public void delete(Long id) {
        scheduleRepo.delete(get(id));
    }

    private void apply(Schedule s, ScheduleRequest r) {
        s.setDayOfWeek(r.dayOfWeek());
        s.setStartTime(r.startTime());
        s.setEndTime(r.endTime());
    }

    private void validateTimes(ScheduleRequest r) {
        if (!r.startTime().isBefore(r.endTime())) {
            throw new BusinessRuleException("Start time must be before end time");
        }
    }
}
