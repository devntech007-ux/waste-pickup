package com.wastepickup.dto;

import jakarta.validation.constraints.NotBlank;

public record ZoneRequest(@NotBlank(message = "must not be blank") String name,
                          String description) {}
