package com.wastepickup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record HouseholdRequest(@NotBlank(message = "must not be blank") String ownerName,
                               @NotBlank(message = "must not be blank") String address,
                               @NotNull(message = "is required") @Positive(message = "must be positive") Long zoneId) {}
