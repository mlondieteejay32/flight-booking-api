package com.mlondi.flightbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull Long seatId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email) {
}
