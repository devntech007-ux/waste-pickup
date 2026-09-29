package com.wastepickup.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/** pickupTime is optional: when omitted the current server time is used. */
public record PickupRequest(@NotNull(message = "is required") @Positive(message = "must be positive") Long householdId,
                            @NotBlank(message = "must not be blank") String collectorName,
                            @NotNull(message = "is required")
                            @Min(value = 0, message = "must be between 0 and 100")
                            @Max(value = 100, message = "must be between 0 and 100") Integer score,
                            LocalDateTime pickupTime) {}
