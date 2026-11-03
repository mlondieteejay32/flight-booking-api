package com.mlondi.flightbooking.dto;

import com.mlondi.flightbooking.entity.Seat;

public record SeatResponse(Long id, String seatNumber) {

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(seat.getId(), seat.getSeatNumber());
    }
}
