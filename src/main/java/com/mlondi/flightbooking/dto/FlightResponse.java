package com.mlondi.flightbooking.dto;

import com.mlondi.flightbooking.entity.Flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightResponse(Long id, String flightNumber, String origin, String destination,
                             LocalDateTime departureTime, LocalDateTime arrivalTime, BigDecimal price) {

    public static FlightResponse from(Flight f) {
        return new FlightResponse(f.getId(), f.getFlightNumber(), f.getOrigin(), f.getDestination(),
                f.getDepartureTime(), f.getArrivalTime(), f.getPrice());
    }
}
