package com.wastepickup.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleRequest(@NotNull(message = "is required") DayOfWeek dayOfWeek,
                              @NotNull(message = "is required") LocalTime startTime,
                              @NotNull(message = "is required") LocalTime endTime) {}
