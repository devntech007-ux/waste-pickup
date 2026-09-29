package com.wastepickup.repository;

import com.wastepickup.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByZone_Id(Long zoneId);

    List<Schedule> findByZone_IdAndDayOfWeek(Long zoneId, DayOfWeek dayOfWeek);
}
