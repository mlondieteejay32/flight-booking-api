package com.mlondi.flightbooking.entity;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestData {

    private TestData() { }

    public static Flight flight() {
        Flight flight = new Flight();
        ReflectionTestUtils.setField(flight, "id", 1L);
        ReflectionTestUtils.setField(flight, "flightNumber", "FB101");
        ReflectionTestUtils.setField(flight, "origin", "JNB");
        ReflectionTestUtils.setField(flight, "destination", "CPT");
        ReflectionTestUtils.setField(flight, "departureTime", LocalDateTime.of(2027, 3, 10, 8, 0));
        ReflectionTestUtils.setField(flight, "arrivalTime", LocalDateTime.of(2027, 3, 10, 10, 5));
        ReflectionTestUtils.setField(flight, "price", new BigDecimal("1499.00"));
        return flight;
    }

    public static Seat seat(boolean booked) {
        Seat seat = new Seat();
        ReflectionTestUtils.setField(seat, "id", 10L);
        ReflectionTestUtils.setField(seat, "flight", flight());
        ReflectionTestUtils.setField(seat, "seatNumber", "1A");
        ReflectionTestUtils.setField(seat, "booked", booked);
        return seat;
    }
}
